package com.manosalaobra.util;

import org.mindrot.jbcrypt.BCrypt;

/** Cifrado de contraseñas (nunca se guardan en texto plano). */
public final class Claves {
    private Claves() { }

    public static String cifrar(String clave) {
        return BCrypt.hashpw(clave, BCrypt.gensalt());
    }

    public static boolean coincide(String clave, String hash) {
        return BCrypt.checkpw(clave, hash);
    }
}
