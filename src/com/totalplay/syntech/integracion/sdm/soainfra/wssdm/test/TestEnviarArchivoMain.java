/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalplay.syntech.integracion.sdm.soainfra.wssdm.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.apache.tomcat.util.codec.binary.Base64;

/**
 *
 * @author Jorge Zavala Navarro
 */
public class TestEnviarArchivoMain {

    public String leerArchivoTOBase64(String nombreArchivo) {

        Base64 base64 = new Base64();

        /*----------------ARCHIVOS------------------*/
        File file = new File(nombreArchivo);
        byte[] fileArray = new byte[(int) file.length()];
        InputStream inputStream;

        String encodedFile = "";
        try {
            inputStream = new FileInputStream(file);
            inputStream.read(fileArray);
            encodedFile = base64.encodeToString(fileArray);
        } catch (Exception e) {
            // Manejar Error
            e.printStackTrace();
        }
        // System.out.println(encodedFile);

        return encodedFile;
    }
    
public static void main(String...parm){
        // String archivo = "F:\\Virtuales\\Windows Server 2012 - Service Desk BDD\\vmware-0.log";
        // Archivo de 259751 bytes si se adjuntó correctamente
        // String archivo = "F:\\Software\\DesarrolloSistemas\\Diseño\\PowerDesigner\\CrackPoweDesigner.rar";
        // Archivo de 601418 bytes si se adjuntó correctamente
        String archivo = "F:\\CA-Technologies\\Anteriores\\CA\\virtualwin2008x64\\vmware.log";
        
        TestEnviarArchivoMain bean = new TestEnviarArchivoMain();
        System.out.println(bean.leerArchivoTOBase64(archivo));
        
        // Invocamos la clase para enviar la información
        
        
        
        
        
                
    }    

}
