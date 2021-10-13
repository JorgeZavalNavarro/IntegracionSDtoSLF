/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.totalplay.syntech.integracion.sdm.soainfra.wssdm.keys;

/**
 * 
 * @author Jorge Zavala Navarro
 */
public class ApplicationKeys {
    
    // Definición de archivos de configuración
    public static final String ARCHIVO_PROPIEDADES_WEB = "C:\\APIs\\IntegracionSDtoSLF\\Config\\IntegracionSDtoSLFConfigPrincipal.properties";
    
    // Constantes especiales para las consultas
    public static final int REGISTROS_POR_CONSULTA = 100;
    
    
    // Definición de las llaves de los parámetros de seguridad
    public static final String PROP_JAVAX_NET_SSL_TRUSTSTORE = "javax.net.ssl.trustStore";
    public static final String PROP_JAVAX_NET_SSL_TRUSTSTOREPASSWORD = "javax.net.ssl.trustStorePassword";
    public static final String PROP_JAVAX_NET_SSL_KEYSTORE = "javax.net.ssl.keyStore";
    public static final String PROP_JAVAX_NET_SSL_KEYSTOREPASSWORD = "javax.net.ssl.keyStorePassword";
    public static final String PROP_JAVAX_NET_SSL_KEYSTORETYPE = "javax.net.ssl.keyStoreType";
    
    // Tipos de datos adminitos para el ws de tpe
    public static final String TIPO_ARCHIVO_TEXT_HTML = "text/html";
    public static final String TIPO_ARCHIVO_IMAGE_JPEG = "image/jpeg";
    public static final String TIPO_ARCHIVO_APPLICATION_PDF = "application/pdf";
    
    // lA SIGUIENTES PROPIEDADES SE MANDAN A ARCHIVO DE CONFIGURACIOÓN
    // public static final String IDENTIFICADOR_ARCHIVO_REPLICADO_SL = "-ARCHIVO REPLICADO EN SALESFORCE-";


}
