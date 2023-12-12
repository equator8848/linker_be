package xyz.equator8848.linker.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Equator
 * @Date: 2021/11/24 23:02
 **/

@Slf4j
public class CmdUtil {
    public static String exec(String cmdStr) throws IOException {
        Runtime run = Runtime.getRuntime();
        Process process = run.exec(cmdStr);
        InputStream in = process.getInputStream();
        InputStreamReader reader = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(reader);
        StringBuilder sb = new StringBuilder();
        String message;
        while ((message = br.readLine()) != null) {
            sb.append(message);
        }
        return sb.toString();
    }

    /**
     * 执行命令、同时实时输出结果
     * /usr/share/nginx/html/gdc-client download -m gdc_manifest_20200713_140144.txt -d ./temp2 --config settings.ddt
     *
     * @param cmdStr
     * @return
     * @throws IOException
     */
    public static String execWithLog(String cmdStr) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec(cmdStr);
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), Charset.forName("GBK")))) {
            String resultLine;
            while ((resultLine = br.readLine()) != null) {
                if (!StringUtils.isEmpty(resultLine)) {
                    // resultLine = resultLine.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
                    log.info("exec log : {}", resultLine);
                    sb.append(resultLine);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return sb.toString();
    }

    /**
     * 超时时间
     *
     * @param cmdStr
     * @param duration
     * @param timeUnit
     * @return
     * @throws InterruptedException
     */
    public static String execWithTimeout(String cmdStr, long duration, TimeUnit timeUnit) {
        return execWithTimeout(cmdStr.split(" "), duration, timeUnit);
    }

    /**
     * 数组参数
     *
     * @param cmdArr
     * @param duration
     * @param timeUnit
     * @return
     */
    public static String execWithTimeout(String[] cmdArr, long duration, TimeUnit timeUnit) {
        long timeout = timeUnit.toMillis(duration);
        Process process = null;
        StringBuilder sbStd = new StringBuilder();
        long start = System.currentTimeMillis();
        String cmdStr = String.join(" ", cmdArr);
        log.info("execWithTimeout exec {}", cmdStr);
        try {
            process = Runtime.getRuntime().exec(cmdArr);
            BufferedReader brStd = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader brErr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line = null;
            while (true) {
                if (brErr.ready()) {
                    line = brErr.readLine();
                    log.debug(line);
                }
                if (brStd.ready()) {
                    line = brStd.readLine();
                    sbStd.append(line);
                    continue;
                }

                try {
                    process.exitValue();
                    break;
                } catch (IllegalThreadStateException e) {
                    log.debug("{} still running", cmdStr);
                }

                if (System.currentTimeMillis() - start > timeout) {
                    sbStd.append("exec cmd time out");
                    break;
                }
                TimeUnit.MILLISECONDS.sleep(10);
            }
        } catch (IOException | InterruptedException e) {
            log.error("exec cmd error {}", cmdStr, e);
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return sbStd.toString();
    }


}
