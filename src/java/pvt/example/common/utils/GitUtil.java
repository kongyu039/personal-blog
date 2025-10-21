package pvt.example.common.utils;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.transport.*;
import org.eclipse.jgit.util.FS;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

/**
 * 信息：src/java/pvt/example/common/utils/GitUtil.java
 * <p>日期：2025/9/30
 * <p>描述：基于JGit的Git工具类
 */
@Component
public class GitUtil {
    @Value("${git.username}")
    private String usernameArg;
    @Value("${git.password}")
    private String passwordArg;
    @Value("${git.ssh}")
    private String sshArg;
    @Value("${git.url}")
    private String urlArg;
    private static String username;
    private static String password;
    private static String ssh;
    private static String url;
    private static File localPath;
    private static String addIdentityPath;

    @PostConstruct
    private void init() {
        username = usernameArg;
        password = passwordArg;
        ssh = sshArg;
        url = urlArg;
        localPath = AppUtil.getJarDirectory("frontend").toFile();
        addIdentityPath = AppUtil.getJarDirectory("ssh", "id_rsa_kongyu039").toAbsolutePath().toString();
    }

    private static Git gitInit() {
        try {
            // 一 本地 1. git init
            Git git = Git.init().setDirectory(localPath).call();
            // 3. git add .
            git.add().addFilepattern(".").call();
            // 4. git commit -m xxx
            git.commit().setMessage("init").call();
            return git;
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
    }

    public static void pushUrl() {
        try (Git git = gitInit()) {
            // 2. config; git remote add origin http的url
            StoredConfig config = git.getRepository().getConfig();
            config.setString("remote", "origin", "url", url);
            config.save();
            // 二 远程 push 强制推到远程仓库 URL
            UsernamePasswordCredentialsProvider provider = new UsernamePasswordCredentialsProvider(username, password);
            git.push().setRemote("origin").setCredentialsProvider(provider).setRefSpecs(
                    new RefSpec("master")).setForce(true).call();
            git.clean().call();
            git.rm().call();
        } catch (GitAPIException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void pushSSH() {
        try (Git git = gitInit()) {
            // 2. config; git remote add origin http的url
            StoredConfig config = git.getRepository().getConfig();
            config.setString("remote", "origin", "url", ssh);
            config.save();
            // 二 远程 push 强制推到远程仓库 SSH
            SshSessionFactory sshSessionFactory = new JschConfigSessionFactory() {
                @Override
                protected void configure(OpenSshConfig.Host host, Session session) { session.setConfig("StrictHostKeyChecking", "no"); }

                @Override
                protected JSch createDefaultJSch(FS fs) throws JSchException {
                    JSch sch = super.createDefaultJSch(fs);        //keyPath 私钥文件 path
                    sch.addIdentity(addIdentityPath);
                    return sch;
                }
            };
            git.push().setRemote("origin").setTransportConfigCallback(transport -> {
                SshTransport sshTransport = (SshTransport) transport;
                sshTransport.setSshSessionFactory(sshSessionFactory);
            }).setForce(true).setRefSpecs(new RefSpec("master")).call();
            git.clean().call();
            git.rm().call();
        } catch (GitAPIException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
