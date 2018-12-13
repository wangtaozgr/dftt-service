package com.atao.base.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Collection;

import org.apache.commons.beanutils.ConvertUtilsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zpz on 2016/4/10.
 */
public class BeanUtils {
	protected static final Logger logger = LoggerFactory.getLogger(BeanUtils.class);
	public static final ConvertUtilsBean CONVERT = new ConvertUtilsBean();

	public BeanUtils() {
	}

	public static void copy(Object source, Object target) {
		Class oldPoClass = source.getClass();
		Class newPoClass = target.getClass();
		Method[] arr$ = oldPoClass.getMethods();
		int len$ = arr$.length;

		for (int i$ = 0; i$ < len$; ++i$) {
			Method method = arr$[i$];
			if (!method.getName().equals("getClass") && isGetter(method)) {
				try {
					Object ex = method.invoke(source, new Object[0]);
					if (ex != null) {
						if (ex instanceof Collection) {
							if (((Collection) ex).isEmpty()) {
								continue;
							}
						} else if (ex.getClass().isArray()) {
							if (ex instanceof Object[]) {
								if (((Object[]) ((Object[]) ex)).length == 0) {
									continue;
								}
							} else if (ex instanceof byte[]) {
								if (((byte[]) ((byte[]) ex)).length == 0) {
									continue;
								}
							} else if (ex instanceof short[]) {
								if (((short[]) ((short[]) ex)).length == 0) {
									continue;
								}
							} else if (ex instanceof int[]) {
								if (((int[]) ((int[]) ex)).length == 0) {
									continue;
								}
							} else if (ex instanceof long[]) {
								if (((long[]) ((long[]) ex)).length == 0) {
									continue;
								}
							} else if (ex instanceof float[]) {
								if (((float[]) ((float[]) ex)).length == 0) {
									continue;
								}
							} else if (ex instanceof double[]) {
								if (((double[]) ((double[]) ex)).length == 0) {
									continue;
								}
							} else if (ex instanceof char[]) {
								if (((char[]) ((char[]) ex)).length == 0) {
									continue;
								}
							} else if (ex instanceof boolean[] && ((boolean[]) ((boolean[]) ex)).length == 0) {
								continue;
							}
						}

						String methodName = getter2Setter(method.getName());
						Class methodParam = method.getReturnType();

						try {
							Method e = newPoClass.getMethod(methodName, new Class[] { methodParam });
							if (ex instanceof String && StringUtils.isBlank(ex.toString())) {
								ex = null;
							}

							e.invoke(target, new Object[] { ex });
						} catch (NoSuchMethodException var12) {
							logger.warn("目标对象缺少{0}方法", new Object[] { var12.getMessage() });
						}
					}
				} catch (Exception var13) {
					logger.error("VO值对象复制错误", new Object[] { var13 });
				}
			}
		}

	}

	public static boolean isGetter(Method method) {
		String name = method.getName();
		return method.getParameterTypes().length == 0 && !name.equals("getClass")
				&& (name.startsWith("get") || name.startsWith("is")) && name.length() > 3;
	}

	public static String getter2Setter(String methodName) {
		return methodName.startsWith("get") ? methodName.replaceFirst("g", "s")
				: (methodName.startsWith("is") ? methodName.replaceFirst("is", "set") : methodName);
	}

	public static String property2Getter(String property) {
		String firstStr = property.substring(0, 1).toUpperCase();
		String methodName = "get" + firstStr + property.substring(1);
		return methodName;
	}

	public static String getAbsolutePath(String relatedPath) {
		String path = getAbsolutePathFromClass(BeanUtils.class, "../../" + relatedPath);
		File file = new File(path);
		return file.exists() ? path
				: getAbsolutePathFromClass(BeanUtils.class, "../../../../../../../../" + relatedPath);
	}

	public static String getAbsolutePathFromClass(Class<?> clazz) throws IOException {
		String path = null;
		if (clazz == null) {
			throw new NullPointerException();
		} else {
			URL url = getClassLocationURL(clazz);
			if (url != null) {
				path = url.getPath();
				if ("jar".equalsIgnoreCase(url.getProtocol())) {
					try {
						path = (new URL(path)).getPath();
					} catch (MalformedURLException var4) {
						logger.error("获取类的绝对路径错误", new Object[] { var4 });
					}

					int file = path.indexOf("!/");
					if (file != -1) {
						path = path.substring(0, file);
					}
				}

				File file1 = new File(path);
				path = file1.getCanonicalPath();
			}

			return path;
		}
	}

	public static String getAbsolutePathFromClass(Class<?> clazz, String relatedPath) {
		String path = null;

		try {
			String classPath = getAbsolutePathFromClass(clazz);
			File e = new File(classPath);
			String tempPath = e.getParent() + File.separator + relatedPath;
			File file = new File(tempPath);
			path = file.getCanonicalPath();
		} catch (IOException var7) {
			logger.error("以clazz参数类为参照，返回其相对路径所对应的绝对路径错误", new Object[] { var7 });
		}

		return path;
	}

	private static URL getClassLocationURL(Class<?> clazz) {
		if (clazz == null) {
			throw new IllegalArgumentException("null input: clazz");
		} else {
			URL result = null;
			String classAsResource = clazz.getName().replace('.', '/').concat(".class");
			ProtectionDomain pd = clazz.getProtectionDomain();
			if (pd != null) {
				CodeSource classLoader = pd.getCodeSource();
				if (classLoader != null) {
					result = classLoader.getLocation();
				}

				if (result != null && "file".equals(result.getProtocol())) {
					try {
						if (!result.toExternalForm().endsWith(".jar") && !result.toExternalForm().endsWith(".zip")) {
							if ((new File(result.getFile())).isDirectory()) {
								result = new URL(result, classAsResource);
							}
						} else {
							result = new URL(
									"jar:".concat(result.toExternalForm()).concat("!/").concat(classAsResource));
						}
					} catch (MalformedURLException var6) {
						logger.error("获取类的class的路径错误", new Object[] { var6 });
					}
				}
			}

			if (result == null) {
				ClassLoader classLoader1 = clazz.getClassLoader();
				result = classLoader1 != null ? classLoader1.getResource(classAsResource)
						: ClassLoader.getSystemResource(classAsResource);
			}

			return result;
		}
	}

}
