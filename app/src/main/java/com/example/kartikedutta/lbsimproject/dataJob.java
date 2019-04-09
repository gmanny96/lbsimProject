package com.example.kartikedutta.lbsimproject;

class dataJob {
    int jid;
    String name, company, companyMail, desc, tag, time;
    Boolean starred;

    dataJob(int j, String n, String d, String t, String cm, String c, Boolean s, String ti){
        jid = j;
        name = n;
        company = c;
        companyMail = cm;
        desc = d;
        tag = t;
        starred = s;
        time = ti;
    }
}
