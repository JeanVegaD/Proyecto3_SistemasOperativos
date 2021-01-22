/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem.directories;

/**
 *
 * @author Luism
 */
public class IDGenerator {
    public static int getNextId() {
        int random_int = (int)(Math.random() * (100000 - 1 + 1) + 1);
        return random_int;
    }
}
