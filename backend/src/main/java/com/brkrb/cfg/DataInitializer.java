// FILE: src/main/java/com/brkrb/cfg/DataInitializer.java
package com.brkrb.cfg;

import com.brkrb.mdl.Usr;
import com.brkrb.mdl.RbSt;
import com.brkrb.rpo.UsrRpo;
import com.brkrb.rpo.RbStRpo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private UsrRpo usrRpo;
    @Autowired private RbStRpo rbStRpo;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        Optional<Usr> adminOpt = usrRpo.findByUn("admin");
        if (adminOpt.isEmpty()) {
            Usr admin = new Usr();
            admin.un = "admin";
            admin.ph = "admin123";
            admin.s = "admin_secret";
            usrRpo.save(admin);
            
            RbSt adminRabbit = new RbSt();
            adminRabbit.uid = admin.id;
            adminRabbit.earDir = -1;
            adminRabbit.furC = "#8899AA";
            adminRabbit.hAng = 0.0;
            adminRabbit.metaJson = "{\"dir\":-1,\"locked\":false,\"mood\":\"sad\"}";
            rbStRpo.save(adminRabbit);
        }

        Optional<Usr> studentOpt = usrRpo.findByUn("student");
        if (studentOpt.isEmpty()) {
            Usr student = new Usr();
            student.un = "student";
            student.ph = "student123";
            student.s = "student_secret";
            usrRpo.save(student);
            
            RbSt studentRabbit = new RbSt();
            studentRabbit.uid = student.id;
            studentRabbit.earDir = -1;
            studentRabbit.furC = "#8899AA";
            studentRabbit.hAng = 0.0;
            studentRabbit.metaJson = "{\"dir\":-1,\"locked\":false}";
            rbStRpo.save(studentRabbit);
        }
    }
}