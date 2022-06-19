package com.project.team9.security.email;

public interface EmailSender  {
    void send(String to,String email,String subject);
}
