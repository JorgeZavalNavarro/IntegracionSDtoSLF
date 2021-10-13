/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.totalplay.syntech.integracion.sdm.soainfra.wssdm.keys;

import java.util.Hashtable;

/**
 * 
 * @author Jorge Zavala Navarro
 */
public class CodeKeys {
    public static final String CODE_000_OK = "000";
    public static final String CODE_010_SIN_INFORMACION = "010";
    public static final String CODE_110_SIN_CREDENCIALES = "110";
    public static final String CODE_120_SIN_CRETERIA = "120";
    public static final String CODE_210_SERVICE_DESK_UNREACHABLE = "210";
    public static final String CODE_220_DATABASE_UNREACHABLE = "220";
    public static final String CODE_310_SERVICE_DESK_WSERROR = "310";
    public static final String CODE_320_DATABASE_SQLERROR = "320";
    public static final String CODE_330_SERVICE_DESK_WSTIMEOUT = "330";
    public static final String CODE_340_DATABASE_SQLTIMEOUT = "340";
    public static final String CODE_350_DATABASE_QUERYERROR = "350";
    public static final String CODE_950_SERVICE_DESK_WSERROR_CREDS = "950";
    public static final String CODE_960_SERVICE_DESK_ERROR_NC = "960";
    public static final String CODE_970_DATABASE_ERROR_NC = "970";
    public static final String CODE_980_ERROR = "980";

    // Definimos los códigos de los errores correspondientes
    public static Hashtable<String, String> htCodigo = new Hashtable<String, String>();
    
    static{
        // Llenamos el listado de los códigos de retorno
        htCodigo.put(CODE_000_OK, 
                "La consulta se ejecutó satisfactoriamente");
        htCodigo.put(CODE_010_SIN_INFORMACION, 
                "La consulta se ejecutó satisfactoriamente pero no arrojo ningún resultado");
        htCodigo.put(CODE_110_SIN_CREDENCIALES, 
                "No se está recibiendo el usuario o password");
        htCodigo.put(CODE_120_SIN_CRETERIA, 
                "No se está recibiendo el valor del campo para la búsqueda de la información");
        htCodigo.put(CODE_210_SERVICE_DESK_UNREACHABLE, 
                "No se está alcanzando el servidor de ServiceDesk para la validación de las credenciales del usuario");
        htCodigo.put(CODE_220_DATABASE_UNREACHABLE, 
                "No se está alcanzando el servidor de la base de datos para la consulta correspondiente");
        htCodigo.put(CODE_310_SERVICE_DESK_WSERROR, 
                "Se produjo un error en el Servicio Web de Service Desk al intentar validar las credenciales proporcionadas");
        htCodigo.put(CODE_320_DATABASE_SQLERROR, 
                "Se produjo un error en el servidor de la base de datos de Service Desk al intentar realizar la consulta");
        htCodigo.put(CODE_330_SERVICE_DESK_WSTIMEOUT, 
                "Se cumplió el timeout del servicio web de ServiceDesk al intentar validar las credenciales proporcionadas");
        htCodigo.put(CODE_340_DATABASE_SQLTIMEOUT, 
                "Se cumplió el timeout en el servidor de la base de datos o en la base de datos al intentar realizare la consulta correspondiente");
        htCodigo.put(CODE_350_DATABASE_QUERYERROR, 
                "Existe error en el código SQL de la consulta");
        htCodigo.put(CODE_350_DATABASE_QUERYERROR, 
                "Existe error en el código SQL de la consulta");
        htCodigo.put(CODE_950_SERVICE_DESK_WSERROR_CREDS, 
                "Error 500 al ejecutar el servicio web de Service Desk al intentar validar las credenciales del usuario");
        htCodigo.put(CODE_960_SERVICE_DESK_ERROR_NC, 
                "Se produjo un error no conocido al intentar ejecutar el servicio web de service desk al intentar validar el usuario");
        htCodigo.put(CODE_970_DATABASE_ERROR_NC, 
                "Se produjo un error no conocido al intentar ejecutar la consulta en el servidor de la base de datos");
        htCodigo.put(CODE_980_ERROR, 
                "Error del servicio no identificado");
    }
}
