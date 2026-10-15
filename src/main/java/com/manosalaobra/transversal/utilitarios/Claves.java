package com.manosalaobra.transversal.utilitarios;

import org.mindrot.jbcrypt.BCrypt;

public final class Claves {
    private Claves() { }

    public static String cifrar(String clave) {
        return BCrypt.hashpw(clave, BCrypt.gensalt());
    }

    public static boolean coincide(String clave, String hash) {
        return BCrypt.checkpw(clave, hash);
    }
}
