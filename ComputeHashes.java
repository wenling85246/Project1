package edu.cmu.ds;

import javax.servlet.annotation.WebServlet;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class is the servlet that compute two kinds of hash value (MD5 and SHA-256)
 * and present in two forms: as base 64 notation and as hexadecimal text.
 *
 * @author Wen Ling Chang
 */
@WebServlet (
        name = "ComputeHashes",
        urlPatterns = "/ComputeHashes"
)
public class ComputeHashes extends javax.servlet.http.HttpServlet {
    /**
     * Handles the HTTP Get method
     * @param request servlet request
     * @param response servlet response
     * @throws javax.servlet.ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        //Retrieve input string and value of hash method
        String inputText=request.getParameter("inputText");
        String hashMethod=request.getParameter("hashMethod");
        String resultPage;
        //If the input text is not null, compute hash values and get Base 64 and Hexadecimal representation
        //Otherwise, return to the index page.
        if (inputText!= null) {
            //Hash the input text
            byte[] hashValue = computeHashValue(inputText,hashMethod);
            //Convert the hashValue into Base64 and Hexadecimal encoding
            String base64String = getBase64(hashValue);
            String hexadecimalString = getHexadecimal(hashValue);
            //Forward the input text, chosen hash method, and results to the result page
            request.setAttribute("inputText",inputText);
            request.setAttribute("hashMethod",hashMethod);
            request.setAttribute("base64String",base64String);
            request.setAttribute("hexadecimalString",hexadecimalString);
            //Forward to the result page
            resultPage = "result.jsp";
        } else {
            //Return to the index page
            resultPage = "index.jsp";
        }
        request.getRequestDispatcher(resultPage).forward(request,response);
    }

    /**
     * Compute hash value according to the input text and hash method
     * @param inputText text to compute
     * @param hashMethod hash method to use
     * @return hash value
     */
    private byte[] computeHashValue (String inputText, String hashMethod){
        byte[] result = null;
        try {
            byte[] byteText = inputText.getBytes();
            //Initialize
            MessageDigest md = MessageDigest.getInstance(hashMethod);
            //Updates the digest using the specified array of bytes
            md.update(byteText);
            //Completes the hash computation
            result = md.digest();
        } catch (NoSuchAlgorithmException e1) {
            System.out.println(e1.getMessage());
        } catch (Exception e2) {
            System.out.println(e2.getMessage());
        }
        return result;
    }

    /**
     * Convert hash value into Base64 encoding
     * @param bytes byte array of the hash value
     * @return Base64 encoding hash value
     */
    private String getBase64(byte[] bytes){
        //Convert binary array into Base64 notation
        return DatatypeConverter.printBase64Binary(bytes);
    }

    /**
     * Convert hash value into hexadecimal encoding
     * @param bytes byte array of the hash value
     * @return Hexadecimal encoding hash value
     */
    private String getHexadecimal(byte[] bytes){
        //Convert binary array into hexadecimal encoding
        return DatatypeConverter.printHexBinary(bytes);
    }


}
