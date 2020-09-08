package xyz.seiyaya.dubbo.ext;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Adaptive;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.Invocation;

import java.lang.reflect.*;
import java.util.Arrays;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/8/18 10:56
 */
public class AdaptiveInvokeHandler implements InvocationHandler {

    private String defaultExtName;

    public AdaptiveInvokeHandler(String defaultExtName) {
        this.defaultExtName = defaultExtName;
    }

    public Object getProxy(Class<?> clazz) {
        if (!clazz.isInterface()) {
            throw new IllegalStateException("Only create the proxy for interface.");
        }
        return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> type = method.getDeclaringClass();
        if (type.equals(Object.class)) {
            throw new UnsupportedOperationException("Cannot invoke the method of Object");
        }
        Adaptive adaptiveAnnotation = method.getAnnotation(Adaptive.class);
        if (adaptiveAnnotation == null) {
            throw new UnsupportedOperationException("method " + method.toString() + " of interface " + type.getName() + " is not adaptive method!");
        }

        // 获取 URL 数据
        URL url = getUrlData(method, args);
        // 获取 Adaptive 注解值
        String[] value = getAdaptiveAnnotationValue(method);
        // 获取 Invocation 参数
        Object invocation = getInvocationArgument(method, args);

        // 获取拓展名
        String extName = getExtensionName(url, value, (com.alibaba.dubbo.rpc.Invocation)invocation);
        if (StringUtils.isEmpty(extName)) {
            throw new IllegalStateException(
                    "Fail to get extension(" + type.getName() + ") name from url(" + url.toString()
                            + ") use keys(" + Arrays.toString(value) + ")");
        }

        // 获取拓展实例
        Object extension = ExtensionLoader.getExtensionLoader(type).getExtension(extName);
        Class<?> extType = extension.getClass();
        Method targetMethod = extType.getMethod(method.getName(), method.getParameterTypes());
        // 通过反射调用目标方法
        return targetMethod.invoke(extension, args);
    }


    private URL getUrlData(Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        URL url = null;
        Class<?>[] pts = method.getParameterTypes();
        for (int i = 0; i < pts.length; i++) {
            if (pts[i].equals(URL.class)) {
                url = (URL) args[i];
                if (url == null) {
                    throw new IllegalArgumentException("url == null");
                }
                break;
            }
        }

        if (url == null) {
            int urlTypeIndex = -1;
            Method getter = null;

            LBL_PTS:
            for (int i = 0; i < pts.length; ++i) {
                Method[] ms = pts[i].getMethods();
                for (Method m : ms) {
                    String name = m.getName();
                    if ((name.startsWith("get") || name.length() > 3)
                            && Modifier.isPublic(m.getModifiers())
                            && !Modifier.isStatic(m.getModifiers())
                            && m.getParameterTypes().length == 0
                            && m.getReturnType() == URL.class) {

                        urlTypeIndex = i;
                        getter = m;
                        break LBL_PTS;
                    }
                }
            }

            if (urlTypeIndex == -1) {
                throw new IllegalArgumentException("Cannot find URL argument.");
            }

            if (args[urlTypeIndex] == null) {
                throw new IllegalArgumentException(pts[urlTypeIndex].getName() + " argument == null");
            }

            url = (URL) getter.invoke(args[urlTypeIndex]);
            if (url == null) {
                throw new IllegalArgumentException(pts[urlTypeIndex].getName() + " argument " + getter.getName() + "() == null");
            }
        }

        return url;
    }

    private String[] getAdaptiveAnnotationValue(Method method) {
        Adaptive adaptiveAnnotation = method.getAnnotation(Adaptive.class);
        Class type = method.getDeclaringClass();
        if (adaptiveAnnotation == null) {
            throw new IllegalArgumentException("method " + method.toString() + " of interface " + type.getName() + " is not adaptive method!");
        }

        String[] value = adaptiveAnnotation.value();
        if (value.length == 0) {
            char[] charArray = type.getSimpleName().toCharArray();
            StringBuilder sb = new StringBuilder(128);
            for (int i = 0; i < charArray.length; i++) {
                if (Character.isUpperCase(charArray[i])) {
                    if (i != 0) {
                        sb.append(".");
                    }
                    sb.append(Character.toLowerCase(charArray[i]));
                } else {
                    sb.append(charArray[i]);
                }
            }
            value = new String[]{sb.toString()};
        }

        return value;
    }

    private Object getInvocationArgument(Method method, Object[] args) {
        Class<?>[] pts = method.getParameterTypes();
        for (int i = 0; i < pts.length; ++i) {
            if (pts[i].getName().equals("com.alibaba.dubbo.rpc.Invocation")) {
                Object invocation = args[i];
                if (invocation == null) {
                    throw new IllegalArgumentException("invocation == null");
                }
                return invocation;
            }
        }

        return null;
    }

    private String getExtensionName(URL url, String[] value, Invocation invocation) throws Exception {
        String methodName = null;
        boolean hasInvocation = invocation != null;
        if (hasInvocation) {
            Class<?> clazz = invocation.getClass();
            Method method = clazz.getMethod("getMethodName");
            methodName = (String) method.invoke(invocation);
        }
        String extName = null;
        for (int i = 0; i < value.length; i++) {
            if (!"protocol".equals(value[i])) {
                if (hasInvocation) {
                    extName = url.getMethodParameter(methodName, value[i], defaultExtName);
                } else {
                    extName = url.getParameter(value[i]);
                }
            } else {
                extName = url.getProtocol();
            }

            if (StringUtils.isNotEmpty(extName)) {
                break;
            }

            if (i == value.length - 1 && StringUtils.isEmpty(extName)) {
                extName = defaultExtName;
            }
        }

        return extName;
    }
}
