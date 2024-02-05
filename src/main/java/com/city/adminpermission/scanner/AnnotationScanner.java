package com.city.adminpermission.scanner;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang3.StringUtils;

import com.city.adminpermission.annotation.AdminPermission;
import com.city.adminpermission.bean.AdminPermissionRepertory;
import com.city.adminpermission.exception.ScannerPackageNotFoundException;

public class AnnotationScanner {

    public void scannerMain() throws ScannerPackageNotFoundException {
        AdminPermissionRepertory apRepertory = AdminPermissionRepertory.getInstance();
        if (StringUtils.isBlank(apRepertory.getScannerPackage())) {
            throw new ScannerPackageNotFoundException("扫描路径未配置");
        }

        List<String> pgList = getScannerPackages(apRepertory.getScannerPackage());

        apRepertory.setClassAnnos(getClassAnnotations(pgList));
    }

    public List<String> getScannerPackages(String packagePath) {
        String[] packages = packagePath.split(";");

        List<String> pgList = new ArrayList<String>();

        for (int i = 0; i < packages.length; i++) {
            List<String> fpg = new ArrayList<String>();
            fpg.add(packages[i].replace(".", "/"));
            pgList.addAll(getScannerPackage(fpg));
        }
        return pgList;
    }

    public List<String> getScannerPackage(List<String> pgs) {
        System.out.println(pgs);
        List<String> pgList = new ArrayList<String>();
        int len = 0;

        String rootPath = "";
        String protocol = "";
        String filePath = "";

        for (String pg : pgs) {
            int sindex = pg.indexOf("*");
            try {
                if (sindex != -1) {
                    rootPath = pg.substring(0, sindex);
                    URL url = Thread.currentThread().getContextClassLoader().getResource(rootPath);

                    if (url == null) {
                        continue;
                    }
                    protocol = url.getProtocol();
                    System.out.println("protocol:" + protocol);

                    if ("file".equals(protocol)) {
                        filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                        File file = new File(filePath);
                        if (file.isDirectory()) {
                            for (File f : file.listFiles()) {
                                if (f.isDirectory()) {
                                    pgList.add(rootPath + f.getName() + pg.substring(sindex + 1));
                                }
                            }
                        }
                    } else if ("jar".equals(protocol)) {
                        JarFile jarFile = new JarFile(url.getPath());
                        Enumeration<JarEntry> entry = jarFile.entries();
                        JarEntry jarEntry;
                        while (entry.hasMoreElements()) {
                            jarEntry = entry.nextElement();
                            String name = jarEntry.getName();
//                  if (name.charAt(0) == '/') {
//                      name=name.substring(1);
//                  }
                            System.out.println(name);
                        }
                    }
                } else {
                    URL url = Thread.currentThread().getContextClassLoader().getResource(pg);
                    if (url != null) {
                        pgList.add(pg);
                    }
                    len++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (len < pgs.size()) {
            pgs = null;
            pgList = getScannerPackage(pgList);
        }
        pgs = null;
        return pgList;
    }

    public Map<Class<?>, Map<Method, String[]>> getClassAnnotations(List<String> list) {
        Map maps = new HashMap();

        String protocol = "";
        String filePath = "";
        try {
            for (String pg : list) {
                URL url = Thread.currentThread().getContextClassLoader().getResource(pg);
                if (url == null) {
                    continue;
                }
                protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    File file = new File(filePath);
                    if (file.isDirectory()) {
                        File[] fList = file.listFiles(new FileFilter() {
                            public boolean accept(File f) {
                                return (!f.isDirectory()) && (f.getName().endsWith(".class"));
                            }
                        });
                        if (fList != null) {
                            for (File f : fList) {
                                Class cls = Class.forName((pg + "/" + f.getName().substring(0, f.getName().length() - 6)).replace("/", "."));
                                System.out.println(cls);
                                Map annos = getMethodAdminPermission(cls);
                                if (!annos.isEmpty()) {
                                    maps.put(cls, annos);
                                }
                            }
                        }
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return maps;
    }

    public Map<Method, String[]> getMethodAdminPermission(Class<?> cls) {
        Map annos = new HashMap();
        Method[] methods = cls.getMethods();

        for (Method m : methods) {
            Annotation anno = m.getAnnotation(AdminPermission.class);
            if (anno != null) {
                System.out.println(m);
                annos.put(m, ((AdminPermission) anno).value());
            }
        }
        return annos;
    }
}
