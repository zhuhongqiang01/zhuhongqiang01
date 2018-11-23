package com.example.log.controller;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @Author: ex-zhuhongqiang001
 * @Description:
 * @Date: 2018/10/9
 */
@Controller
@RequestMapping(value = "/log")
public class LogController {
    @Autowired
    private HttpServletResponse response;

    @ResponseBody
    @GetMapping(value = "/get", produces = {"application/json;charset=UTF-8"})
    public String get(Integer n,String sec){
        String path = "/tmp/rc.local.log";
        String hostName = "172.31.134.195";
        // String hostName = "103.28.214.7:8080";
        int port = 22;
        String username = "jiang";
        n = n == null?100:n;
        sec = sec == null?"10":sec;
        String pwd = "1001";
        Connection ss = getConnect(hostName, username, pwd, port);
        String s = "";
        if (fileExist(path, ss)) {
            s = readLogFile(path, ss,n);
        }
        response.setHeader("refresh",sec);
        return s;
    }
    public static Connection getConnect(String hostName, String username, String password, int port) {
        Connection conn = new Connection(hostName, port);
        try {
            // 连接到主机
            conn.connect();
            // 使用用户名和密码校验
            boolean isconn = conn.authenticateWithPassword(username, password);
            if (!isconn) {
                System.out.println("用户名称或者是密码不正确");
            } else {
                System.out.println("服务器连接成功.");
                return conn;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static boolean fileExist(String path, Connection conn) {
        if (conn != null) {
            Session ss = null;
            try {
                ss = conn.openSession();
                ss.execCommand("ls -l ".concat(path));
                InputStream is = new StreamGobbler(ss.getStdout());
                BufferedReader brs = new BufferedReader(new InputStreamReader(is));
                String line = "";
                while (true) {
                    String lineInfo = brs.readLine();
                    if (lineInfo != null) {
                        line = line + lineInfo;
                    } else {
                        break;
                    }
                }
                brs.close();
                if (line != null && line.length() > 0 && line.startsWith("-")) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 连接的Session和Connection对象都需要关闭
                if (ss != null) {
                    ss.close();
                }
            }
        }
        return false;
    }
    public static String readLogFile(String path, Connection conn,int n) {
        String log = "";
        if (conn != null) {
            Session ss = null;
            try {
                ss = conn.openSession();
                ss.execCommand("tail -"+n+" ".concat(path));
                InputStream is = new StreamGobbler(ss.getStdout());
                BufferedReader brs = new BufferedReader(new InputStreamReader(is));
                while (true) {
                    String line = brs.readLine();
                    if (line == null) {
                        break;
                    } else {
                        // System.out.println(line);
                        log += line + "\n";
                    }}
                brs.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 连接的Session和Connection对象都需要关闭
                if (ss != null) {
                    ss.close();
                }
                if (conn != null) {
                    conn.close();
                }
            }
        }
        return log;

    }
}
