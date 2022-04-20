package models.environments.menus.mainmenu.submenus;

import models.prototypes.environments.menu.AMenu;
import models.prototypes.environments.menu.AMenuEnvironment;
import models.prototypes.components.menuviews.types.AButtonView;

/**
 * <p></p>
 */
public class MainMenuPage extends AMenu {

    /**
     * <p></p>
     * @param parentModel -
     */
    public MainMenuPage(AMenuEnvironment parentModel) {
        super(parentModel);

        int buttonW = 400, buttonH = (int)(buttonW * .25);

        AButtonView button_newGame = new AButtonView(
                getParentEnvironment(),
                (int)(centerW - (buttonW * .5f)),
                175,
                buttonW,
                buttonH)
        {

            @Override
            public boolean onClick(float x, float y) {
                if(!isInBounds(x, y)) {
                    return false;
                }

                getParentEnvironment().push(new NewGamePage(getParentEnvironment()));

                return true;
            }
        };
        button_newGame.setText("New Game");
        button_newGame.setImageScaling(AButtonView.ImageScale.FIT_CENTERED);

        AButtonView button_continueGame = new AButtonView(
                getParentEnvironment(),
                (int)(centerW - (buttonW * .5f)),
                290,
                buttonW,
                buttonH) {
            @Override
            public boolean onClick(float x, float y) {
                if(!isInBounds(x, y)) {
                    return false;
                }

                navigateToLevelSelectPage();

                return true;
            }
        };
        button_continueGame.setText("Continue Game");
        button_continueGame.setImageScaling(AButtonView.ImageScale.FIT_CENTERED);

        AButtonView button_options = new AButtonView(
                getParentEnvironment(),
                (int)(centerW - (buttonW * .5f)),
                405,
                buttonW,
                buttonH) {
            @Override
            public boolean onClick(float x, float y) {
                if(!isInBounds(x, y)) {
                    return false;
                }

                getParentEnvironment().push(new OptionsPage(getParentEnvironment()));

                return true;
            }
        };
        button_options.setText("Options");
        button_options.setImageScaling(AButtonView.ImageScale.FIT_CENTERED);

        AButtonView button_help = new AButtonView(
                getParentEnvironment(),
                (int)(centerW - (buttonW * .5f)),
                520,
                buttonW,
                buttonH) {
            @Override
            public boolean onClick(float x, float y) {
                if(!isInBounds(x, y)) {
                    return false;
                }

                getParentEnvironment().push(new HelpPage(getParentEnvironment()));

                return true;
            }
        };
        button_help.setText("Help");
        button_help.setImageScaling(AButtonView.ImageScale.FIT_CENTERED);

        AButtonView button_quit = new AButtonView(
                getParentEnvironment(),
                (int)(centerW - (buttonW * .5f)),
                800,
                buttonW,
                buttonH) {
            @Override
            public boolean onClick(float x, float y) {
                if(!isInBounds(x, y)) {
                    return false;
                }
                System.exit(0);

                return true;
            }
        };
        button_quit.setText("Quit to Desktop");
        button_quit.setBackgroundImage(image_buttonRect);
        button_quit.setImageScaling(AButtonView.ImageScale.FIT_CENTERED);

        addComponent(button_newGame);
        addComponent(button_continueGame);
        addComponent(button_options);
        addComponent(button_help);
        addComponent(button_quit);

    }

    /**
     * <p></p>
     */
    public void navigateToLevelSelectPage() {
        getParentEnvironment().push(new LevelSelectPage(getParentEnvironment()));
    }

}
