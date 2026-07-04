package io.github.serenibyss.gemsack;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class GemSackPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(GemSackPlugin.class);
		RuneLite.main(args);
	}
}
