package calendar.view;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JButton;

/**
 * Factory class for creating consistently styled buttons with modern design.
 * Reduces code duplication across dialog and view classes.
 */
public final class ButtonFactory {

  private ButtonFactory() {
    
  }

  /**
   * Creates a styled action button with consistent appearance, hover effects, and tooltips.
   *
   * @param text the button text
   * @param bgColor the background color
   * @param action the action to perform on click
   * @return the configured button
   */
  public static JButton createActionButton(String text, Color bgColor, Runnable action) {
    return createActionButton(text, bgColor, null, action);
  }

  /**
   * Creates a styled action button with tooltip.
   *
   * @param text the button text
   * @param bgColor the background color
   * @param tooltip the tooltip text (can be null)
   * @param action the action to perform on click
   * @return the configured button
   */
  public static JButton createActionButton(String text, Color bgColor, String tooltip,
                                           Runnable action) {
    JButton button = new JButton(text);
    button.setFont(UIFonts.ACTION_BUTTON_FONT);
    button.setPreferredSize(UIConstants.ACTION_BUTTON_SIZE);
    button.setBackground(bgColor);
    // Use white text for colored buttons, dark text for light buttons
    button.setForeground(isDarkColor(bgColor) ? CalendarTheme.BUTTON_TEXT_LIGHT 
                                               : CalendarTheme.BUTTON_TEXT);
    button.setFocusPainted(false);
    button.setOpaque(true);
    button.setBorderPainted(true);
    button.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createRaisedBevelBorder(),
        BorderFactory.createEmptyBorder(5, 15, 5, 15)
    ));
    
    if (tooltip != null && !tooltip.isEmpty()) {
      button.setToolTipText(tooltip);
    }
    
    // Add hover effects
    Color originalBg = bgColor;
    Color hoverBg = brightenColor(bgColor, 15);
    Color pressedBg = darkenColor(bgColor, 15);
    
    button.addMouseListener(new java.awt.event.MouseAdapter() {
      @Override
      public void mouseEntered(java.awt.event.MouseEvent e) {
        button.setBackground(hoverBg);
      }
      
      @Override
      public void mouseExited(java.awt.event.MouseEvent e) {
        button.setBackground(originalBg);
      }
      
      @Override
      public void mousePressed(java.awt.event.MouseEvent e) {
        button.setBackground(pressedBg);
      }
      
      @Override
      public void mouseReleased(java.awt.event.MouseEvent e) {
        button.setBackground(hoverBg);
      }
    });
    
    if (action != null) {
      button.addActionListener(e -> action.run());
    }
    return button;
  }

  /**
   * Creates a styled dialog button (save/create).
   *
   * @param text the button text
   * @param action the action to perform on click
   * @return the configured button
   */
  public static JButton createSaveButton(String text, Runnable action) {
    return createDialogButton(text, CalendarTheme.BUTTON_SAVE_BG, 
                             "Save changes", action);
  }

  /**
   * Creates a styled dialog button (cancel).
   *
   * @param text the button text
   * @param action the action to perform on click
   * @return the configured button
   */
  public static JButton createCancelButton(String text, Runnable action) {
    return createDialogButton(text, CalendarTheme.BUTTON_CANCEL_BG,
                             "Cancel and discard changes", action);
  }

  /**
   * Creates a styled dialog button with specified color and hover effects.
   *
   * @param text the button text
   * @param bgColor the background color
   * @param tooltip the tooltip text
   * @param action the action to perform on click
   * @return the configured button
   */
  private static JButton createDialogButton(String text, Color bgColor, String tooltip,
                                           Runnable action) {
    JButton button = new JButton(text);
    button.setFont(UIFonts.ACTION_BUTTON_FONT);
    button.setPreferredSize(UIConstants.DIALOG_BUTTON_SIZE);
    button.setBackground(bgColor);
    // Use white text for colored buttons, dark text for light buttons
    button.setForeground(isDarkColor(bgColor) ? CalendarTheme.BUTTON_TEXT_LIGHT 
                                               : CalendarTheme.BUTTON_TEXT);
    button.setFocusPainted(false);
    button.setOpaque(true);
    button.setBorderPainted(true);
    button.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createRaisedBevelBorder(),
        BorderFactory.createEmptyBorder(8, 20, 8, 20)
    ));
    
    if (tooltip != null && !tooltip.isEmpty()) {
      button.setToolTipText(tooltip);
    }
    
    // Add hover effects
    Color originalBg = bgColor;
    Color hoverBg = brightenColor(bgColor, 20);
    Color pressedBg = darkenColor(bgColor, 20);
    
    button.addMouseListener(new java.awt.event.MouseAdapter() {
      @Override
      public void mouseEntered(java.awt.event.MouseEvent e) {
        button.setBackground(hoverBg);
      }
      
      @Override
      public void mouseExited(java.awt.event.MouseEvent e) {
        button.setBackground(originalBg);
      }
      
      @Override
      public void mousePressed(java.awt.event.MouseEvent e) {
        button.setBackground(pressedBg);
      }
      
      @Override
      public void mouseReleased(java.awt.event.MouseEvent e) {
        button.setBackground(hoverBg);
      }
    });
    
    if (action != null) {
      button.addActionListener(e -> action.run());
    }
    return button;
  }

  /**
   * Brightens a color by a given amount.
   *
   * @param color the original color
   * @param amount the amount to brighten (0-255)
   * @return the brightened color
   */
  private static Color brightenColor(Color color, int amount) {
    int r = Math.min(255, color.getRed() + amount);
    int g = Math.min(255, color.getGreen() + amount);
    int b = Math.min(255, color.getBlue() + amount);
    return new Color(r, g, b);
  }

  /**
   * Darkens a color by a given amount.
   *
   * @param color the original color
   * @param amount the amount to darken (0-255)
   * @return the darkened color
   */
  private static Color darkenColor(Color color, int amount) {
    int r = Math.max(0, color.getRed() - amount);
    int g = Math.max(0, color.getGreen() - amount);
    int b = Math.max(0, color.getBlue() - amount);
    return new Color(r, g, b);
  }

  /**
   * Determines if a color is dark (requires light text for contrast).
   *
   * @param color the color to check
   * @return true if the color is dark
   */
  private static boolean isDarkColor(Color color) {
    // Calculate relative luminance
    double luminance = (0.299 * color.getRed() + 0.587 * color.getGreen() 
                       + 0.114 * color.getBlue()) / 255;
    return luminance < 0.5;
  }
}


