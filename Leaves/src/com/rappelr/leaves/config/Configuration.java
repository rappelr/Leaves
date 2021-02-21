package com.rappelr.leaves.config;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.val;

public class Configuration {
	
	static final YamlUtil util = new YamlUtil();
	
	@Getter(AccessLevel.PROTECTED)
	private final File file;
	
	@Getter
	private YamlConfiguration source;
	
	public Configuration(final String name, final JavaPlugin plugin, final boolean copy) {
		this(new File(plugin.getDataFolder(), name), plugin, copy ? name : null);
	}
	
	public Configuration(final File file, final JavaPlugin plugin, final String sourceFile) {
		this.file = file;
		
		if(!file.exists()) {
			file.getParentFile().mkdirs();
			if(sourceFile != null)
				util.copy("res/" + sourceFile, file.getAbsolutePath());
			else
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
				
		source = new YamlConfiguration();
	}
	
	public boolean load() {
		try {
			val f = new YamlConfiguration();
			f.load(file);
			source = f;
			return true;
        } catch (IOException | InvalidConfigurationException exception) {
        	exception.printStackTrace();
        	return false;
        }
	}

	public boolean contains(String key) {
		return source.contains(key);
	}

	public void set(String key, Object value) {
		source.set(key, value);
	}

	public void save() {
		try {
			getSource().save(getFile());
        } catch (IOException e) {
        	e.printStackTrace();
        }
	}
}
