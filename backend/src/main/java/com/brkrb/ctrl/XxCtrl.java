// FILE: src/main/java/com/brkrb/ctrl/XxCtrl.java
package com.brkrb.ctrl;

import com.brkrb.svc.ExSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

@RestController
@RequestMapping("/api/cfg")
public class XxCtrl {
    @Autowired private ExSvc eS;

    @PostMapping("/imp")
    public ResponseEntity<?> p1(@RequestBody String xml, @RequestHeader(value="X-Cid", required=false) String uid) {
        try {
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            f.setFeature("http://xml.org/sax/features/external-general-entities", true);
            f.setFeature("http://xml.org/sax/features/external-parameter-entities", true);
            f.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false);
            f.setXIncludeAware(true);
            f.setExpandEntityReferences(true);
            
            DocumentBuilder b = f.newDocumentBuilder();
            Document d = b.parse(new InputSource(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8))));
            String txt = d.getDocumentElement().getTextContent();
            
            Long u = uid != null ? Long.parseLong(uid) : 1L;
            boolean containsFileContent = txt.contains("root:") || txt.contains("bin:") || txt.contains("/etc/passwd") || txt.length() > 100;
            
            if (containsFileContent) {
                eS.t(u, "v5_xxe", xml);
            }
            
            return ResponseEntity.ok("parsed: " + txt);
        } catch (Exception x) {
            return ResponseEntity.badRequest().body("xml_err: " + x.getMessage());
        }
    }
}