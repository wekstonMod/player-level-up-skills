package net.Wekston.player_skills.menu;

import net.Wekston.player_skills.Stamina.registry.StaminaAttributes;
import net.Wekston.player_skills.network.NetworkHandler;
import net.Wekston.player_skills.PlayerData.PlayerDataManager;
import net.Wekston.player_skills.menu.button.TextureButton;
import net.Wekston.player_skills.menu.button.TextButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SkillsMenu extends Screen {
    private static final ResourceLocation INV =
            new ResourceLocation("player_skills", "textures/gui/inventory.png");
    private final Player player;
    private final int guiWidth = 216;
    private final int guiHeight = 176;
    private int x;
    private int y;
    private int guiLeft;
    private int guiTop;
    private int addhealth = 0;
    private int addstamina = 0;
    private int addavoidance = 0;
    private int addpower = 0;
    public SkillsMenu(Player player) {
        super(Component.literal(""));
        this.player = player;

    }
    @Override
    protected void init() {
        super.init();
        this.guiLeft = (this.width - guiWidth) / 2;
        this.guiTop = (this.height - guiHeight) / 2;

        this.x = this.width / 2 - 5;
        this.y = this.height / 2;

        this.addRenderableWidget(new TextureButton(
                x - 55, y + 70 , 120, 15,
                Component.translatable("player_skills.level_up"),
                button -> onAddSkill()
        ));
    }

    public int getTotalExpHealth() {
        int healthLvl = PlayerDataManager.getHealthLevel(player);
        int totalHealthExp = 0;
        for (int i = 1; i <= addhealth; i++) {
            int level = healthLvl + i;
            totalHealthExp += level * level * 5;
        }
        return totalHealthExp;
    }

    public int getTotalExpStamina() {
        int staminalvl = PlayerDataManager.getStaminaLevel(player);
        int totalStaminaExp = 0;
        for (int i = 1; i <= addstamina; i++) {
            int level = staminalvl + i;
            totalStaminaExp += level * level * 5;
        }
        return totalStaminaExp;
    }
    public int getTotalExpPower() {
        int powerlvl = PlayerDataManager.getPowerLevel(player);
        int totalPowerExp = 0;
        for (int i = 1; i <= addpower; i++) {
            int level = powerlvl + i;
            totalPowerExp += level * level * 5;
        }
        return totalPowerExp;
    }
    public int getTotalExpAvoidance() {
        int Avoidancelvl = PlayerDataManager.getAvoidanceLevel(player);
        int totalAvoidanceExp = 0;
        for (int i = 1; i <= addavoidance; i++) {
            int level = Avoidancelvl + i;
            totalAvoidanceExp += level * level * 5;
        }
        return totalAvoidanceExp;
    }
    public int getTotalExpAll() {
        int totalAllExp = getTotalExpAvoidance() +
                getTotalExpPower() + getTotalExpStamina() +
                getTotalExpHealth();
        return totalAllExp;
    }


    private void AddStaminaNum() {
        if(PlayerDataManager.getStaminaLevel(player) + addstamina < 50) {
            addstamina += 1;
            init();
        }
    }

    private void RemoveStaminaNum() {
        if (addstamina > 0) {
            addstamina -= 1;
            init();
        }
    }
    private void AddHealthNum() {
        if(PlayerDataManager.getHealthLevel(player) + addhealth < 50) {
            addhealth += 1;
            init();
        }
    }

    private void RemoveHealthNum() {
        if (addhealth > 0) {
            addhealth -= 1;
            init();
        }
    }

    private void AddAvoidanceNum() {
        if(PlayerDataManager.getAvoidanceLevel(player) + addavoidance < 50) {
            addavoidance += 1;
            init();
        }
    }

    private void RemoveAvoidanceNum() {
        if (addavoidance > 0) {
            addavoidance -= 1;
            init();
        }
    }

    private void AddPowerNum() {
        if(PlayerDataManager.getPowerLevel(player) + addpower < 50) {
            addpower += 1;
            init();
        }
    }

    private void RemovePowerNum() {
        if (addpower > 0) {
            addpower -= 1;
            init();
        }
    }

    private void onAddSkill() {
        if (player.totalExperience >= getTotalExpAll()) {
            if (addhealth > 0) {
                int healthToAdd = addhealth;
                int xp = getTotalExpHealth();
                NetworkHandler.sendHealthUpgradePacket(healthToAdd, xp);
                addhealth = 0;
            }
            if (addstamina > 0) {
                int staminaToAdd = addstamina;
                int xp = getTotalExpStamina();
                NetworkHandler.sendStaminaUpgradePacket(staminaToAdd, xp);
                addstamina = 0;
            }
            if (addpower > 0) {
                int powerToAdd = addpower;
                int xp = getTotalExpPower();
                NetworkHandler.sendPowerUpgradePacket(powerToAdd, xp);
                addpower = 0;
            }
            if (addavoidance > 0) {
                int AvoidanceToAdd = addavoidance;
                int xp = getTotalExpAvoidance();
                NetworkHandler.sendAvoidanceUpgradePacket(AvoidanceToAdd, xp);
                addavoidance = 0;
            }
        }
        else {
            Minecraft mc = Minecraft.getInstance();
            mc.player.playSound(SoundEvents.VILLAGER_NO, 1.0F, 1.0F);
        }
        init();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);

        graphics.blit(INV,
                guiLeft, guiTop,
                0, 0,
                216, 176,
                216, 176);

        //Хп
        if (PlayerDataManager.getHealthLevel(player) < 50) {
            this.addRenderableWidget(new TextButton(
                    x + 70, y - 60, 10, 10,
                    Component.literal("-"),
                    button -> RemoveHealthNum()
            ));
            this.addRenderableWidget(new TextButton(
                    x + 100, y - 60, 10, 10,
                    Component.literal("+"),
                    button -> AddHealthNum()
            ));
        }
        //выносливость
        if (PlayerDataManager.getStaminaLevel(player) < 50) {
            this.addRenderableWidget(new TextButton(
                    x + 70, y - 45, 10, 10,
                    Component.literal("-"),
                    button -> RemoveStaminaNum()
            ));
            this.addRenderableWidget(new TextButton(
                    x + 100, y - 45, 10, 10,
                    Component.literal("+"),
                    button -> AddStaminaNum()
            ));
        }

        //сила
        if (PlayerDataManager.getPowerLevel(player) < 50) {
            this.addRenderableWidget(new TextButton(
                    x + 70, y - 30, 10, 10,
                    Component.literal("-"),
                    button -> RemovePowerNum()
            ));
            this.addRenderableWidget(new TextButton(
                    x + 100, y - 30, 10, 10,
                    Component.literal("+"),
                    button -> AddPowerNum()
            ));
        }

        //уклонение
        if (PlayerDataManager.getAvoidanceLevel(player) < 50) {
            this.addRenderableWidget(new TextButton(
                    x + 70, y - 15, 10, 10,
                    Component.literal("-"),
                    button -> RemoveAvoidanceNum()
            ));
            this.addRenderableWidget(new TextButton(
                    x + 100, y - 15, 10, 10,
                    Component.literal("+"),
                    button -> AddAvoidanceNum()
            ));
        }
        int healthLvl = PlayerDataManager.getHealthLevel(player);
        int totalLevels = PlayerDataManager.getTotalLevels(player);
        int staminalvl = PlayerDataManager.getStaminaLevel(player);
        int powerlvl = PlayerDataManager.getPowerLevel(player);
        int Avoidancelvl = PlayerDataManager.getAvoidanceLevel(player);

        Font font = Minecraft.getInstance().font;
        Component LevelText = Component.translatable("player_skills.level", totalLevels);
        int widtgLevel =  (x - 95) + (70 - font.width(LevelText)) / 2;

        graphics.drawString(
                this.font,
                Component.translatable("player_skills.level", totalLevels),
                widtgLevel, y - 77,
                0xFFFFFF
        );
        Component AttributeText = Component.translatable("player_skills.attribute");

        int widtgAttributes =  x + 10 + (74 - font.width(AttributeText)) / 2;
        graphics.drawString(
                this.font,
                Component.translatable("player_skills.attribute"),
                widtgAttributes, y - 77,
                0xFFFFFF
        );

        if (player != null) {
            graphics.drawString(
                    this.font,
                    Component.translatable("player_skills.health"),
                    x - 85, y + 10,
                    0xFFFFFF
            );
            graphics.drawString(
                    this.font,
                    Component.translatable("player_skills.stamina"),
                    x - 85, y + 25,
                    0xFFFFFF
            );
            graphics.drawString(
                    this.font,
                    Component.translatable("player_skills.power"),
                    x - 85, y + 40,
                    0xFFFFFF
            );
            graphics.drawString(
                    this.font,
                    Component.translatable("player_skills.avoidance"),
                    x - 85, y + 55,
                    0xFFFFFF
            );
            double currentMaxHealth = player.getMaxHealth();
            double newMaxHealth = currentMaxHealth * Math.pow(1.05, addhealth);

            AttributeInstance currentMaxStamina = player.getAttribute(StaminaAttributes.MAX_STAMINA.get());

            double currentMaxLevelStamina = currentMaxStamina.getBaseValue();
            double newMaxStamina = currentMaxLevelStamina * Math.pow(1.07, addstamina);

            double MaxPower = 1 * Math.pow(1.02, powerlvl - 1);
            double newMaxPower = 1 * Math.pow(1.02, powerlvl+ addpower);

            int currentMaxLevelAvoidance = PlayerDataManager.getAvoidanceLevel(player);
            int newMaxAvoidance = currentMaxLevelAvoidance + addavoidance;

            int HealthWidth = font.width(String.format("%.1f",currentMaxHealth));
            int StaminaWidth = font.width(String.format("%.1f",currentMaxLevelStamina));
            int PowerWidth = font.width(String.format("%.1f",MaxPower));
            int AvoidanceWidth = font.width(String.valueOf(currentMaxLevelAvoidance));


            graphics.drawString(
                    this.font,
                    Component.literal(String.format("%.1f", currentMaxHealth)),
                    x + 50 - HealthWidth, y + 10,
                    0xFFFFFF
            );
            graphics.drawString(
                    this.font,
                    Component.translatable(String.format("%.1f",currentMaxLevelStamina)),
                    x + 50 - StaminaWidth, y + 25,
                    0xFFFFFF
            );
            graphics.drawString(
                    this.font,
                    Component.translatable(String.format("%.1f", MaxPower)),
                    x + 50 - PowerWidth, y + 40,
                    0xFFFFFF
            );
            graphics.drawString(
                    this.font,
                    Component.translatable(String.valueOf(currentMaxLevelAvoidance)),
                    x + 50 - AvoidanceWidth, y + 55,
                    0xFFFFFF
            );
            //>

            graphics.drawString(
                    this.font,
                    Component.literal(">"),
                    x + 53, y + 10,
                    0xFF25D73C
            );
            graphics.drawString(
                    this.font,
                    Component.literal(">"),
                    x + 53, y + 25,
                    0xFF25D73C
            );

            graphics.drawString(
                    this.font,
                    Component.literal(">"),
                    x + 53, y + 40,
                    0xFF25D73C
            );
            graphics.drawString(
                    this.font,
                    Component.literal(">"),
                    x + 53, y + 55,
                    0xFF25D73C
            );




            //второе

            graphics.drawString(
                    this.font,
                    Component.literal(String.format("%.1f", newMaxHealth)),
                    x + 60, y + 10,
                    0xFFFFFF
            );
            graphics.drawString(
                    this.font,
                    Component.translatable(String.format("%.1f", newMaxStamina)),
                    x + 60, y + 25,
                    0xFFFFFF
            );
            graphics.drawString(
                    this.font,
                    Component.translatable(String.format("%.1f", newMaxPower)),
                    x + 60, y + 40,
                    0xFFFFFF
            );
            graphics.drawString(
                    this.font,
                    Component.translatable(String.valueOf(newMaxAvoidance)),
                    x + 60, y + 55,
                    0xFFFFFF
            );

            String total = String.valueOf(player.totalExperience);
            String cost = String.valueOf(getTotalExpAll());
            int totalWidth = x - 100 + (80 - font.width(total)) / 2;
            int costWidth = x - 100 + (80 - font.width(cost)) / 2;

            graphics.drawString(
                    this.font,
                    Component.literal(total),
                    totalWidth, y - 49,
                    0xFFFFFF
            );
            graphics.drawString(
                    this.font,
                    Component.literal(cost),
                    costWidth, y - 38,
                    0xFFFFFF
            );

            int displayLevelHealth = healthLvl + addhealth;
            String levelTextHealth = (healthLvl >= 50) ? "MAX" : String.format("%d", displayLevelHealth);

            int widthHealth = font.width(levelTextHealth);
            int widthHealthPercent = x + 80 + (20 - widthHealth) / 2;
            graphics.drawString(
                    this.font,
                    Component.literal(levelTextHealth),
                    widthHealthPercent, y - 60,
                    0xFFFFFF
            );
            int displayLevelStamina = staminalvl + addstamina;
            String levelTextStamina = (staminalvl >= 50) ? "MAX" : String.format("%d", displayLevelStamina);

            int widthStamina = font.width(levelTextStamina);
            int widthStaminaPercent = x + 80 + (20 - widthStamina) / 2;
            graphics.drawString(
                    this.font,
                    Component.literal(levelTextStamina),
                    widthStaminaPercent, y - 45,
                    0xFFFFFF
            );

            int displayLevelPower = powerlvl + addpower;
            String levelTextPower = (powerlvl >= 50) ? "MAX" : String.format("%d",
                    displayLevelPower);

            int widthPower = font.width(levelTextPower);
            int widthPowerPercent = x + 80 + (20 - widthPower) / 2;
            graphics.drawString(
                    this.font,
                    Component.literal(levelTextPower),
                    widthPowerPercent, y - 30,
                    0xFFFFFF
            );

            int displayLevelAvoidance = Avoidancelvl + addavoidance;
            String levelTextAvoidance = (Avoidancelvl >= 50) ? "MAX" : String.format("%d",
                    displayLevelAvoidance);

            int widthAvoidance = font.width(levelTextAvoidance);
            int widthAvoidancePercent = x + 80 + (20 - widthAvoidance) / 2;
            graphics.drawString(
                    this.font,
                    Component.literal(levelTextAvoidance),
                    widthAvoidancePercent, y - 15,
                    0xFFFFFF
            );


            graphics.drawString(
                    this.font,
                    Component.translatable("player_skills.health"),
                    x - 15, y - 60,
                    0xFFFFFF
            );
            graphics.drawString(
                    this.font,
                    Component.translatable("player_skills.stamina"),
                    x - 15, y - 45,
                    0xFFFFFF
            );
            graphics.drawString(
                    this.font,
                    Component.translatable("player_skills.power"),
                    x - 15 , y - 30,
                    0xFFFFFF
            );
            graphics.drawString(
                    this.font,
                    Component.translatable("player_skills.avoidance"),
                    x - 15, y - 15,
                    0xFFFFFF
            );
        }

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(null);
    }
}