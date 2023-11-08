package br.com.west.chain.utils;

import br.com.west.chain.Main;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class RegisterListeners {

    public void setupListeners() {
        try {
            ImmutableSet<ClassPath.ClassInfo> infoList = ClassPath.from(Main.class.getClassLoader()).getTopLevelClassesRecursive("br.com.west.chain.listeners");
            for (ClassPath.ClassInfo info : infoList) {
                Class<?> cls = Class.forName(info.getName());
                if (Listener.class.isAssignableFrom(cls)) {
                    Class<Listener> ex = (Class<Listener>) cls;
                    Bukkit.getPluginManager().registerEvents(ex.newInstance(), Main.getInstance());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
