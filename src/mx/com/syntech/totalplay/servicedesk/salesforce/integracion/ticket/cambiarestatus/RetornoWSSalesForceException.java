/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.syntech.totalplay.servicedesk.salesforce.integracion.ticket.cambiarestatus;

/**
 *
 * @author dell
 */
public class RetornoWSSalesForceException extends Exception {

        public RetornoWSSalesForceException(String message) {
            super(message);
        }

        public RetornoWSSalesForceException(Throwable cause) {
            super(cause);
        }

    
}
