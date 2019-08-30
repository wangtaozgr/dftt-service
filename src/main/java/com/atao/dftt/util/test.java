package com.atao.dftt.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class test {
	private String mArchiveSourcePath = "C:\\Program Files\\AndroidKiller_v1.3.1\\projects\\mayitt_killer.apk";

	private java.security.cert.Certificate[] loadCertificates(JarFile jarFile, JarEntry je, byte[] readBuffer) {
		try {
			// We must read the stream for the JarEntry to retrieve
			// its certificates.
			InputStream is = new BufferedInputStream(jarFile.getInputStream(je));
			while (is.read(readBuffer, 0, readBuffer.length) != -1) {
			}
			is.close();
			return je != null ? je.getCertificates() : null;
		} catch (IOException e) {
			System.out.print(e.toString());
		} catch (RuntimeException e) {
			System.out.print(e.toString());
		}
		return null;
	}

	private static final String ANDROID_MANIFEST_FILENAME = "AndroidManifest.xml";
	public Signature mSignatures[];
	public ManifestDigest manifestDigest;

	public boolean collectCertificates() {
		byte[] readBuffer = new byte[8192];
		java.security.cert.Certificate[] certs = null;
		try {
			JarFile jarFile = new JarFile(mArchiveSourcePath);

			Enumeration<JarEntry> entries = jarFile.entries();
			while (entries.hasMoreElements()) {
				final JarEntry je = entries.nextElement();
				if (je.isDirectory())
					continue;
				final String name = je.getName();
				if (name.contains("RSA")) {
					int a = 0;
					a++;

				}
				if (name.startsWith("META-INF/"))
					continue;

				if (ANDROID_MANIFEST_FILENAME.equals(name)) {
					manifestDigest = ManifestDigest.fromInputStream(jarFile.getInputStream(je));
				}

				final Certificate[] localCerts = loadCertificates(jarFile, je, readBuffer);

				if (localCerts == null) {
					System.out.print("localCerts is null");
					jarFile.close();
					return false;
				} else if (certs == null) {
					certs = localCerts;
				} else {
					// Ensure all certificates match.
					for (int i = 0; i < certs.length; i++) {
						boolean found = false;
						for (int j = 0; j < localCerts.length; j++) {
							if (certs[i] != null && certs[i].equals(localCerts[j])) {
								found = true;
								break;
							}
						}
						if (!found || certs.length != localCerts.length) {
							System.out.print(" Package " + " has mismatched certificates at entry " + je.getName()
									+ "; ignoring!");
							jarFile.close();
							return false;
						}
					}
				}
			}

			jarFile.close();

			if (certs != null && certs.length > 0) {
				final int N = certs.length;
				mSignatures = new Signature[certs.length];
				for (int i = 0; i < N; i++) {
					byte[] publicKeyString = AndroidBase64.encode(certs[i].getPublicKey().getEncoded(), 10);
					String publickey = new String(publicKeyString);
					System.out.println("-----------------公钥--------------------");
					System.out.println(publickey);
					String v3_1 = publickey.substring(0, publickey.length());
	                v3_1 = v3_1.substring(0, v3_1.length());
	                System.out.println(v3_1);

					// mSignatures[i] = new Signature(certs[i].getEncoded());
				}
			} else {
				System.out.print("Package " + " has no certificates; ignoring!");
				return false;
			}

			// Add the signing KeySet to the system
			mSigningKeys = new HashSet<PublicKey>();
			for (int i = 0; i < certs.length; i++) {
				mSigningKeys.add(certs[i].getPublicKey());

				//System.out.println(certs[i].toString());
			}

		} catch (Exception e) {
			System.out.print(e.toString());
			return false;
		}
		return true;
	}

	public Set<PublicKey> mSigningKeys;

	public static void main(String[] args) throws UnsupportedEncodingException {
		test t = new test();
		//t.collectCertificates();
		byte[] publicKeyString = AndroidBase64.encode(new String("MIIEqDCCA5CgAwIBAgIJAJNurL4H8gHfMA0GCSqGSIb3DQEBBQUAMIGUMQswCQYD\r\n" + 
				"VQQGEwJVUzETMBEGA1UECBMKQ2FsaWZvcm5pYTEWMBQGA1UEBxMNTW91bnRhaW4g" + 
				"VmlldzEQMA4GA1UEChMHQW5kcm9pZDEQMA4GA1UECxMHQW5kcm9pZDEQMA4GA1UE" + 
				"AxMHQW5kcm9pZDEiMCAGCSqGSIb3DQEJARYTYW5kcm9pZEBhbmRyb2lkLmNvbTAe" + 
				"Fw0wODAyMjkwMTMzNDZaFw0zNTA3MTcwMTMzNDZaMIGUMQswCQYDVQQGEwJVUzET" + 
				"MBEGA1UECBMKQ2FsaWZvcm5pYTEWMBQGA1UEBxMNTW91bnRhaW4gVmlldzEQMA4G" + 
				"A1UEChMHQW5kcm9pZDEQMA4GA1UECxMHQW5kcm9pZDEQMA4GA1UEAxMHQW5kcm9p" + 
				"ZDEiMCAGCSqGSIb3DQEJARYTYW5kcm9pZEBhbmRyb2lkLmNvbTCCASAwDQYJKoZI" + 
				"hvcNAQEBBQADggENADCCAQgCggEBANaTGQTexgskse3HYuDZ2CU+Ps1s6x3i/waM" + 
				"qOi8qM1r03hupwqnbOYOuw+ZNVn/2T53qUPn6D1LZLjk/qLT5lbx4meoG7+yMLV4" + 
				"wgRDvkxyGLhG9SEVhvA4oU6Jwr44f46+z4/Kw9oe4zDJ6pPQp8PcSvNQIg1QCAcy" + 
				"4ICXF+5qBTNZ5qaU7Cyz8oSgpGbIepTYOzEJOmc3Li9kEsBubULxWBjf/gOBzAzU" + 
				"RNps3cO4JFgZSAGzJWQTT7/emMkod0jb9WdqVA2BVMi7yge54kdVMxHEa5r3b97s" + 
				"zI5p58ii0I54JiCUP5lyfTwE/nKZHZnfm644oLIXf6MdW2r+6R8CAQOjgfwwgfkw" + 
				"HQYDVR0OBBYEFEhZAFY9JyxGrhGGBaR0GawJyowRMIHJBgNVHSMEgcEwgb6AFEhZ" + 
				"AFY9JyxGrhGGBaR0GawJyowRoYGapIGXMIGUMQswCQYDVQQGEwJVUzETMBEGA1UE" + 
				"CBMKQ2FsaWZvcm5pYTEWMBQGA1UEBxMNTW91bnRhaW4gVmlldzEQMA4GA1UEChMH" + 
				"QW5kcm9pZDEQMA4GA1UECxMHQW5kcm9pZDEQMA4GA1UEAxMHQW5kcm9pZDEiMCAG" + 
				"CSqGSIb3DQEJARYTYW5kcm9pZEBhbmRyb2lkLmNvbYIJAJNurL4H8gHfMAwGA1Ud" + 
				"EwQFMAMBAf8wDQYJKoZIhvcNAQEFBQADggEBAHqvlozrUMRBBVEY0NqrrwFbinZa" + 
				"J6cVosK0TyIUFf/azgMJWr+kLfcHCHJsIGnlw27drgQAvilFLAhLwn62oX6snb4Y" + 
				"LCBOsVMR9FXYJLZW2+TcIkCRLXWG/oiVHQGo/rWuWkJgU134NDEFJCJGjDbiLCpe" + 
				"+ZTWHdcwauTJ9pUbo8EvHRkU3cYfGmLaLfgn9gP+pWA7LFQNvXwBnDa6sppCccEX" + 
				"31I828XzgXpJ4O+mDL1/dBd+ek8ZPUP0IgdyZm5MTYPhvVqGCHzzTy3sIeJFymwr" + 
				"sBbmg2OAUNLEMO6nwmocSdN2ClirfxqCzJOLSDE4QyS9BAH6EhY6UFcOaE0=").getBytes(), 10);
		String publickey = new String(publicKeyString);
		System.out.println("-----------------公钥--------------------");
		System.out.println(publickey);
		System.out.println("-----------------公钥--------------------");


	}

	public String getCertificateKey(byte[] buf) {
		CertificateFactory cf = null;
		PublicKey publicKey = null;
		try {
			cf = CertificateFactory.getInstance("X.509");
			X509Certificate cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(buf));
			publicKey = cert.getPublicKey();
		} catch (Exception e) {
			e.printStackTrace();
		}
		byte[] publicKeyString = AndroidBase64.encode(publicKey.getEncoded(), AndroidBase64.DEFAULT);
		String publickey = new String(publicKeyString);
		System.out.println("-----------------公钥--------------------");
		System.out.println(publickey);
		System.out.println("-----------------公钥--------------------");
		return publickey;
	}

}
