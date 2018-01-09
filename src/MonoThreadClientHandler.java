import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.util.Scanner;

public class MonoThreadClientHandler implements Runnable{

    @Override
    public void run() {
        try {
            DataInputStream in = new DataInputStream(clientDialog.getInputStream());
            String vals=in.readUTF();
            if(vals.charAt(0)=='0'){
                vals=vals.substring(2,vals.length());
                System.out.println(vals);
            try {
                Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/charity?verifyServerCertificate=false&useSSL=true&useUnicode=true&characterEncoding=utf-8","root","admin");
                System.out.println("Connected");
                Statement st=con.createStatement();
                st.executeUpdate("insert into users(logins,passwords,firstnames,lastnames,region,age) values"+" ("+vals+");");
                DataOutputStream out=new DataOutputStream(clientDialog.getOutputStream());
                out.writeInt(1);
            } catch (SQLException e) {
                DataOutputStream out=new DataOutputStream(clientDialog.getOutputStream());
                out.writeInt(0);
            }
            }
            else if(vals.charAt(0)=='1'){
                vals=vals.substring(2,vals.length());
                System.out.println(vals);
                String login="";
                String passwd="";
                for(int i=0;i<vals.length();i++){
                    if(vals.charAt(i)==','){
                        login=vals.substring(0,i);
                        passwd=vals.substring(i+1,vals.length());
                        break;
                    }
                }
               try {
                   Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/charity?verifyServerCertificate=false&useSSL=true&useUnicode=true&characterEncoding=utf-8", "root", "admin");
                   Statement st = con.createStatement();
                   ResultSet rs=st.executeQuery("SELECT * FROM users WHERE logins="+"'"+login+"'");
                   while (rs.next()){
                       if(rs.getString(2).equals(passwd)){
                           DataOutputStream out=new DataOutputStream(clientDialog.getOutputStream());
                           out.writeUTF(rs.getString(3)+","+rs.getString(4)+","+rs.getString(5)+","+rs.getString(6));
                       }else throw new SQLException();
                   }

               }
               catch (SQLException e){
                   DataOutputStream out=new DataOutputStream(clientDialog.getOutputStream());
                   out.writeUTF("0");
               }
            }
        }


        catch (IOException e){

        }
    }
    private static Socket clientDialog;

    public MonoThreadClientHandler(Socket client) {
        MonoThreadClientHandler.clientDialog = client;
    }

}
