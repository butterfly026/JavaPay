
package com.city.city_collector.configuration;


import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;

import com.city.adminpermission.annotation.AdminPermission;
import com.city.adminpermission.bean.AdminPermissionRepertory;
import com.google.gson.Gson;

/**
 * @Description: 注解扫描器
 */
@Component
public class SpringAnnotationScanner {

    @Autowired
    private ResourceLoader resourceLoader;

    public void scannerMain() throws IOException, ClassNotFoundException {
        AdminPermissionRepertory apRepertory = AdminPermissionRepertory.getInstance();
        if (StringUtils.isBlank(apRepertory.getScannerPackage())) {
            System.out.println("扫描路径未配置");
        }
        String packagePath = apRepertory.getScannerPackage();
        String[] packages = packagePath.split(";");


        ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        MetadataReaderFactory metaReader = new CachingMetadataReaderFactory(resourceLoader);

        Map<Class<?>, Map<Method, String[]>> maps = new HashMap<Class<?>, Map<Method, String[]>>();

        for (String pkg : packages) {
            pkg = "classpath*:" + pkg.replace(".", "/") + "/*.class";
            Resource[] resources = resolver.getResources(pkg);
            for (Resource r : resources) {
                MetadataReader reader = metaReader.getMetadataReader(r);

                AnnotationMetadata annotationMetadata = reader.getAnnotationMetadata();
//                Map<String, Object> annotationAttributes = annotationMetadata.getAnnotationAttributes(AdminPermission.class.getCanonicalName());
//                System.out.println(AdminPermission.class.getCanonicalName());
                //类名
//                System.out.println(annotationMetadata.getClassName());
                Class<?> cls = getClass().getClassLoader().loadClass(annotationMetadata.getClassName());
//                System.out.println(cls);

//                Set < MethodMetadata > annotatedMethods = annotationMetadata.getAnnotatedMethods(AdminPermission.class.getCanonicalName());
//                Iterator < MethodMetadata > it = annotatedMethods.iterator();
//                
//                Map < Method, String[] > methods=new HashMap<Method, String[]>();
//                
//                while(it.hasNext()) {
//                    MethodMetadata mm=it.next();
//                    //com.city.city_collector.admin.city.controller.ConfigController view  java.lang.String
//                    System.out.println(mm.getDeclaringClassName()+" "+mm.getMethodName()+"  "+mm.getReturnTypeName());
//                    Map < String, Object> map=mm.getAnnotationAttributes(AdminPermission.class.getCanonicalName());
//                    System.out.println("方法属性:"+map.get("value"));
//                    
//                    String[] permissions=(String[])map.get("value");
//                    System.out.println(new Gson().toJson(permissions));
//                }
                Map<Method, String[]> annos = getMethodAdminPermission(cls);
                if (!annos.isEmpty()) {
                    maps.put(cls, annos);
                }
            }
        }

        apRepertory.setClassAnnos(maps);
    }


    public Map<Method, String[]> getMethodAdminPermission(Class<?> cls) {
        Map<Method, String[]> annos = new HashMap<Method, String[]>();
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
