package com.totalplay.syntech.integracion.sdm.soainfra.wssdm.test;

import mx.com.syntech.totalplay.servicedesk.salesforce.integracion.connbdd.ConnectorBDDConsultasBean;
import mx.com.syntech.totalplay.servicedesk.salesforce.integracion.core.CoreBean;
import com.totalplay.syntech.integracion.sdm.soainfra.wssdm.keys.ApplicationKeys;
import com.totalplay.syntech.integracion.sdm.soainfra.wssdm.props.AppPropsBean;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;

/**
 *
 * @author Jorge Zavala Navarro
 */
public class CrearArchivosDummyBean extends CoreBean {
    
    private static final String uNoLock = "  WITH (NOLOCK) ";
/**
    public void generarArchivosDummy() {
        Connection conn = null;
        
        String rutaRaiz = "C:\\Temporal\\Syntech\\tpe\\PruebasDos";
        try {
            conn = ConnectorBDDConsultasBean.getConectionServiceDesk();

            String sqlBuscarInfo
                    = " SELECT TOP 10000 ADJUNTO.PERSID, TICKET.ID AS CLAVE_TICKET, TICKET.PERSID AS PERSID_TICKET,\n"
                    + "        TICKET.REF_NUM AS NUMERO_TICKET, RELACION.ID AS CLAVE_RELACION,\n"
                    + "        ADJUNTO.ID AS CLAVE_ADJUNTO, ADJUNTO.REL_FILE_PATH AS CARPETA_REPOSITORIO,\n"
                    + "        ADJUNTO.DEL AS BANDERA, ADJUNTO.ORIG_FILE_NAME AS NOMBRE_ARCHIVO_ORIGINAL, \n"
                    + "        ADJUNTO.FILE_NAME AS NOMBRE_ARCHIVO_REPOSITORIO, ADJUNTO.ZIP_FLAG AS BANDERA_COMPRIMIDO, \n"
                    + "        ADJUNTO.STATUS AS ESTATUS_ARCHIVO, ADJUNTO.DESCRIPTION AS DESCRIPCION_ADJUNTO, \n"
                    + "        ADJUNTO.CREATED_BY AS CREADO_POR\n"
                    + "   FROM ATTMNT AS ADJUNTO " + uNoLock + ", "
                    + "        USP_LREL_ATTACHMENTS_REQUESTS AS RELACION" + uNoLock + " , "
                    + "        CALL_REQ AS TICKET " + uNoLock + "  \n"
                    + "  WHERE ADJUNTO.ID = RELACION.ATTMNT \n"
                    + "    AND TICKET.PERSID = RELACION.CR \n"
                    // + "    AND TICKET.EXTERNAL_SYSTEM_TICKET IS NOT NULL \n"
                    // Se cambia el nombre del campo del tickeyt externo 22/06/2021
                    // por el campo de zfolio_dbw_sf el cual se va a registrar el numero
                    // de ticket correspondiente a salesforce
                    + "    AND TICKET.zfolio_dbw_sf IS NOT NULL \n"
                    + "  ORDER BY TICKET.ID DESC";
            System.out.println("Query a ejecutar...");
            System.out.println(sqlBuscarInfo);
            

            String sqlMarcarSalesforce
                    = "UPDATE ATTMNT " + "\n"
                    + "   SET DESCRIPTION = '" + AppPropsBean.getPropsVO().getAdjuntoMarcaReplicadoSalesforce() + "'" + "\n"
                    + " WHERE ID = ";

            PreparedStatement psBuscarInfo = conn.prepareCall(sqlBuscarInfo);
            Statement stMarcarAdjunto = conn.createStatement();
            ResultSet rsBuscarInfo = psBuscarInfo.executeQuery();
            int i = 1;
            String rutaArchivo = null;
            String rutaArchivoGZ = null;
            String descripcionAdjunto = null;
            Integer idAdjunto = null;
            File fileRuta = null;
            File fileArchivo = null;
            FileOutputStream fos = null;
            while (rsBuscarInfo.next()) {
                // Obtenemos la ruta del archivo

                rutaArchivo = rutaRaiz + "\\" + rsBuscarInfo.getString("CARPETA_REPOSITORIO");
                descripcionAdjunto = rsBuscarInfo.getString("DESCRIPCION_ADJUNTO");
                idAdjunto = rsBuscarInfo.getInt("CLAVE_ADJUNTO");
                fileRuta = new File(rutaArchivo);
                fileRuta.mkdirs();
                rutaArchivoGZ = rutaArchivo + "\\" + rsBuscarInfo.getString("NOMBRE_ARCHIVO_REPOSITORIO");
                rutaArchivo = rutaArchivo + "\\" + rsBuscarInfo.getString("NOMBRE_ARCHIVO_REPOSITORIO") + ".ngz";
                System.out.println("Creando el archivo " + i++ + rutaArchivo);
                fileArchivo = new File(rutaArchivo);

                fos = new FileOutputStream(fileArchivo);
                fos.write("Mi nombre es Jorege Zavala Navarro".getBytes());
                fos.write("\n".getBytes());
                fos.write(("Este archivo se llama: " + rsBuscarInfo.getString("NOMBRE_ARCHIVO_ORIGINAL")).getBytes());
                fos.write("\n".getBytes());
                if (descripcionAdjunto != null && !descripcionAdjunto.isEmpty()
                        && !descripcionAdjunto.trim().toLowerCase().equals("null")) {
                    fos.write(("Descripción del adjunto: " + descripcionAdjunto).getBytes());
                    fos.write("\n".getBytes());
                } else {
                    // Actualizamos la descripción de este archivo, marcandolo como proveniente
                    // de salesforced
                    System.out.println("Marcar adjunto: " + idAdjunto);
                    stMarcarAdjunto.executeUpdate(sqlMarcarSalesforce + idAdjunto);

                }

                // complñetamos y cerramos el archivo
                fos.flush();
                fos.close();

                // Comprimimos el archivo
                gzipCompression(rutaArchivo, rutaArchivoGZ);

                // Si la descripción del adjunto es nula la marcamos 
                // como proveniente de salkesforce en la tabla de la base
                conn.commit();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } catch (Throwable th) {
            th.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            } catch (Throwable th) {
                th.printStackTrace();

            }
        }
    }

    public static void main(String... parmas) {

        CrearArchivosDummyBean bean = new CrearArchivosDummyBean();
        bean.generarArchivosDummy();

    }

    public static boolean gzipCompression(String filePath, String resultFilePath) throws IOException {
        // System.out.println(" gzipCompression -> Compression start!");
        InputStream fin = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        GzipCompressorOutputStream gcos = null;
        try {
            fin = Files.newInputStream(Paths.get(filePath));
            bis = new BufferedInputStream(fin);
            fos = new FileOutputStream(resultFilePath);
            bos = new BufferedOutputStream(fos);
            gcos = new GzipCompressorOutputStream(bos);
            byte[] buffer = new byte[1024];
            int read = -1;
            while ((read = bis.read(buffer)) != -1) {
                gcos.write(buffer, 0, read);
            }
        } finally {
            if (gcos != null) {
                gcos.close();
            }
            if (bos != null) {
                bos.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (bis != null) {
                bis.close();
            }
            if (fin != null) {
                fin.close();
            }
        }
        // System.out.println(" gzipCompression -> Compression end!");
        return true;
    }
**/
}
