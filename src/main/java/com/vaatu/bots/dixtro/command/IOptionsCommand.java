package com.vaatu.bots.dixtro.command;

import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public interface IOptionsCommand {
    List<OptionData> getOptions();
}
