package com.ysf.ipadress.security;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.Locale;
import java.util.Properties;

public class LdapAuthenticator {
    private final String[] RETURNING_ATTRS={"cn"};
    private String FILTER="(sAMAccountName=%s)";
    private static LdapAuthenticator ldap;

    private final DirContext context;
    private final SearchControls searchControls;

    public static LdapAuthenticator getInstance() throws NamingException {
        if (ldap==null){
            ldap=new LdapAuthenticator();
        }
        return ldap;
    }
    private LdapAuthenticator() throws NamingException {
        Properties properties=new Properties();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");

        properties.put(Context.PROVIDER_URL, "ldap://tmo.local/dc=tmo,dc=local");

        properties.put(Context.SECURITY_AUTHENTICATION, "simple");

        context=new InitialDirContext(properties);

        searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        searchControls.setReturningAttributes(RETURNING_ATTRS);
    }
    public String authenticate(String username,String password) throws NamingException {
        context.addToEnvironment(Context.SECURITY_PRINCIPAL,username+"@tmo.local");
        context.addToEnvironment(Context.SECURITY_CREDENTIALS,password);
        String filter=String.format(FILTER,username);
        NamingEnumeration<SearchResult> search = context.search("", filter, searchControls);
        SearchResult next = search.next();
        if (next!=null){
            //sadece ad soyad bilgisi çekildiği için bu şekilde yazıldı
            return ((String) next.getAttributes().get(RETURNING_ATTRS[0]).get(0)).toUpperCase(Locale.ROOT);
        }
        return null;
    }
}
