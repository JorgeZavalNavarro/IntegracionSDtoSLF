package mx.com.syntech.totalplay.servicedesk.salesforce.integracion.core;

import java.sql.Timestamp;
import org.apache.log4j.Category;

/**
 *
 * @author Jorge Zavala Navarro
 */
public class CoreBean {
    
    // Constantes de la clase
    private static final Category log = Category.getInstance(CoreBean.class);

    protected Timestamp hoy() {
        return new Timestamp((new java.util.Date()).getTime());
    }

    
    protected String getValorArgumentos(String[] args, String propiedad) {
        
        String retorno = null;
        
        // Escaneamos los argumentos
        log.debug("Buscar el valor de: " + propiedad);
        
        for (int i = 0; i < args.length; i++) {
            log.debug("Buscando " + propiedad + " ==>" + args[i]);
            if (args[i].trim().equals(propiedad)) {
                retorno = args[i + 1];
                break;
            }
        }
        
        return retorno;
    }
    
    protected void seeArgs(String...args){
        log.debug("Procesando el siguiente arreglo...");
        if(args!=null && args.length>0){
            for(int i=0; i<args.length; i++){
                log.debug("elem: " + i + " ==> " + args[i]);
            }
        }else{
            log.debug("No se está recibiendo información.");
        }
    }
}
