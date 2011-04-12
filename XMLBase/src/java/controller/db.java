/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Database;
import model.DatabaseFactory;

/**
 *
 * @author yavi
 */
public class db extends HttpServlet {

    Database db;
    Map<String,OperationHandler> handlers;

    @Override
    public void init() throws ServletException {
        final String XMLDataFile = "database.xml";
        final String XMLSchemaFile = "database.xsd";

        String XMLDataFileURL;
        String XMLSchemaFileURL;
        try {
            XMLDataFileURL = "http://localhost:8080" + this.getServletContext().getContextPath() + "/" + XMLDataFile;
            XMLSchemaFileURL = "http://localhost:8080" + this.getServletContext().getContextPath() + "/" + XMLSchemaFile;
            db = DatabaseFactory.getXMLDatabase(XMLDataFileURL,XMLSchemaFileURL);
        } catch (IOException ex) {
            Logger.getLogger(db.class.getName()).log(Level.SEVERE, null, ex);
        }

        handlers = new HashMap<String,OperationHandler>();

        OperationHandler h;
        h = new GetOperationHandler();
        handlers.put(h.Register(), h);
        h = new RefOperationHandler();
        handlers.put(h.Register(), h);
        h = new AddOperationHandler();
        handlers.put(h.Register(), h);
        h = new DelOperationHandler();
        handlers.put(h.Register(), h);

        super.init();
    }


    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            if(request.getParameter("op") == null)
            {
                out.write("Error - no operation!");
                return;
            }
            OperationHandler h = handlers.get(request.getParameter("op"));
            h.Handle(request, response);
        } finally { 
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Operation Callbacks">
    private interface OperationHandler {
        public void Handle(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException ;
        public String Register();
    }

    private class GetOperationHandler implements OperationHandler {
        public void Handle(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException
        {
            //op=get
            //type=autor|ksiazka
            //value1=tytul|imieinazwisko
            //regex=1|0
            String type = request.getParameter("type");
            String table = "";
            
            PrintWriter out = response.getWriter();

            if(type.equals("autor")){
                table="autorzy";
                if(request.getParameter("value1").equals("all"))
                {
                    out.write(db.getRecords(table).getTextContent());
                } else {
                    out.write(db.getRecordByNameKey(table, "imieNazwisko", request.getParameter("value1")).getTextContent());
                }
            }
            else if(type.equals("ksiazka")){
                table="ksiazki";
                if(request.getParameter("value1").equals("all"))
                {
                    out.write(db.getRecords(table).getTextContent());
                } else {
                    out.write(db.getRecordByNameKey(table, "tytul", request.getParameter("value1")).getTextContent());
                }
            }
            out.close();
        }

        public String Register(){return "get";}
    }

    private class RefOperationHandler implements OperationHandler {
        public void Handle(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException
        {
            
        }

        public String Register(){return "ref";}
    }

    private class AddOperationHandler implements OperationHandler {
        public void Handle(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException
        {

        }

        public String Register(){return "add";}
    }

    private class DelOperationHandler implements OperationHandler {
        public void Handle(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException
        {

        }

        public String Register(){return "del";}
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
