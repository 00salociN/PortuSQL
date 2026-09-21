package Telas;

import java.awt.Color;

import javax.swing.UIManager;

/**
 * Tema escuro global do PortuSQL.
 *
 * <p>as telas ja pintam as proprias cores componente a componente, mas
 * varios elementos nativos do Swing (menus abertos, popups, scrollbars,
 * abas, caixas de dialogo, tabelas) usam as cores do {@link UIManager}
 * do tema atual do sistema -- que costumam ser claras. esta classe
 * centraliza os valores padrao do tema escuro para todos esses
 * componentes, deixando o visual consistente em qualquer sistema
 * operacional e Look and Feel.
 *
 * <p>chame {@link #aplicar()} uma unica vez, antes de criar qualquer
 * janela (geralmente no main da aplicacao).
 *
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 */
public final class Tema {

    /** fundo principal das janelas */
    public static final Color FUNDO = new Color(0x1E, 0x1E, 0x1E);
    /** fundo de paineis intermediarios (barras, navegador, abas) */
    public static final Color FUNDO_PAINEL = new Color(0x25, 0x25, 0x25);
    /** fundo do editor de texto */
    public static final Color FUNDO_EDITOR = new Color(0x1A, 0x1A, 0x1A);
    /** texto principal */
    public static final Color TEXTO = new Color(0xE0, 0xE0, 0xE0);
    /** texto secundario / placeholder */
    public static final Color TEXTO_SECUNDARIO = new Color(0x90, 0x90, 0x90);
    /** bordas das regiões */
    public static final Color BORDA = new Color(0x33, 0x33, 0x33);
    /** fundo de selecao (arvore, tabela) */
    public static final Color SELECAO = new Color(0x2A, 0x3A, 0x4A);
    /** verde de destaque (logo, icone ativo) */
    public static final Color VERDE = new Color(0x2E, 0xCC, 0x71);
    /** azul de destaque */
    public static final Color AZUL = new Color(0x34, 0x98, 0xDB);

    private Tema() {
        // classe utilitaria: sem instancias
    }

    /**
     * aplica os valores do tema escuro no {@link UIManager}.
     * pode ser chamada mais de uma vez sem efeitos colaterais.
     */
    public static void aplicar() {
        // containers e textos
        p("Panel.background", FUNDO);
        p("Panel.foreground", TEXTO);
        p("Label.background", FUNDO);
        p("Label.foreground", TEXTO);

        // menu e popups
        p("MenuBar.background", FUNDO_PAINEL);
        p("MenuBar.foreground", TEXTO);
        p("Menu.background", FUNDO_PAINEL);
        p("Menu.foreground", TEXTO);
        p("Menu.selectionBackground", SELECAO);
        p("Menu.selectionForeground", TEXTO);
        p("Menu.opaque", Boolean.TRUE);
        p("MenuItem.background", FUNDO_PAINEL);
        p("MenuItem.foreground", TEXTO);
        p("MenuItem.selectionBackground", SELECAO);
        p("MenuItem.selectionForeground", TEXTO);
        p("MenuItem.opaque", Boolean.TRUE);
        p("PopupMenu.background", FUNDO_PAINEL);
        p("PopupMenu.foreground", TEXTO);
        p("PopupMenu.border", new javax.swing.border.LineBorder(BORDA));

        // dicas de ferramenta
        p("ToolTip.background", FUNDO_PAINEL);
        p("ToolTip.foreground", TEXTO);
        p("ToolTip.border", new javax.swing.border.LineBorder(BORDA));

        // scrollbars e scrollpanes
        p("ScrollBar.background", FUNDO_PAINEL);
        p("ScrollBar.foreground", new Color(0x4A, 0x4A, 0x4A));
        p("ScrollBar.thumb", new Color(0x4A, 0x4A, 0x4A));
        p("ScrollBar.thumbDarkShadow", new Color(0x33, 0x33, 0x33));
        p("ScrollBar.thumbHighlight", new Color(0x55, 0x55, 0x55));
        p("ScrollBar.thumbLightShadow", new Color(0x55, 0x55, 0x55));
        p("ScrollBar.track", FUNDO_PAINEL);
        p("ScrollBar.trackHighlight", FUNDO_PAINEL);
        p("ScrollPane.background", FUNDO_PAINEL);
        p("ScrollPane.foreground", TEXTO);
        p("ScrollPane.border", new javax.swing.border.LineBorder(BORDA));
        p("Viewport.background", FUNDO_PAINEL);
        p("Viewport.foreground", TEXTO);

        // abas
        p("TabbedPane.background", FUNDO_PAINEL);
        p("TabbedPane.foreground", TEXTO);
        p("TabbedPane.selected", FUNDO_EDITOR);
        p("TabbedPane.selectedForeground", TEXTO);
        p("TabbedPane.unselectedBackground", FUNDO_PAINEL);
        p("TabbedPane.contentAreaColor", FUNDO_PAINEL);
        p("TabbedPane.tabAreaBackground", FUNDO_PAINEL);
        p("TabbedPane.borderHighlightColor", BORDA);
        p("TabbedPane.contentBorderInsets", new java.awt.Insets(0, 0, 0, 0));

        // arvore
        p("Tree.background", FUNDO_PAINEL);
        p("Tree.textBackground", FUNDO_PAINEL);
        p("Tree.textForeground", TEXTO);
        p("Tree.selectionBackground", SELECAO);
        p("Tree.selectionForeground", TEXTO);
        p("Tree.editorBackground", FUNDO_EDITOR);
        p("Tree.line", BORDA);
        p("Tree.hash", BORDA);

        // tabela (usada nos resultados futuros)
        p("Table.background", FUNDO_PAINEL);
        p("Table.foreground", TEXTO);
        p("Table.selectionBackground", SELECAO);
        p("Table.selectionForeground", TEXTO);
        p("Table.gridColor", BORDA);
        p("TableHeader.background", FUNDO_PAINEL);
        p("TableHeader.foreground", TEXTO);

        // areas e campos de texto
        p("TextArea.background", FUNDO_EDITOR);
        p("TextArea.foreground", TEXTO);
        p("TextArea.caretForeground", TEXTO);
        p("TextField.background", FUNDO_EDITOR);
        p("TextField.foreground", TEXTO);
        p("TextField.caretForeground", TEXTO);
        p("TextField.selectionBackground", SELECAO);
        p("TextPane.background", FUNDO_EDITOR);
        p("TextPane.foreground", TEXTO);
        p("TextPane.caretForeground", TEXTO);
        p("EditorPane.background", FUNDO_EDITOR);
        p("EditorPane.foreground", TEXTO);
        p("FormattedTextField.background", FUNDO_EDITOR);
        p("FormattedTextField.foreground", TEXTO);
        p("PasswordField.background", FUNDO_EDITOR);
        p("PasswordField.foreground", TEXTO);

        // listas e combos
        p("List.background", FUNDO_PAINEL);
        p("List.foreground", TEXTO);
        p("List.selectionBackground", SELECAO);
        p("List.selectionForeground", TEXTO);
        p("ComboBox.background", FUNDO_PAINEL);
        p("ComboBox.foreground", TEXTO);
        p("ComboBox.selectionBackground", SELECAO);
        p("ComboBox.selectionForeground", TEXTO);
        p("ComboBox.buttonBackground", FUNDO_PAINEL);

        // botoes e caixas de dialogo
        p("Button.background", new Color(0x33, 0x33, 0x33));
        p("Button.foreground", TEXTO);
        p("Button.select", new Color(0x3D, 0x3D, 0x3D));
        p("Button.focus", VERDE);
        p("OptionPane.background", FUNDO_PAINEL);
        p("OptionPane.messageForeground", TEXTO);
        p("OptionPane.buttonBackground", new Color(0x33, 0x33, 0x33));
        p("OptionPane.buttonForeground", TEXTO);
        p("CheckBox.background", FUNDO_PAINEL);
        p("CheckBox.foreground", TEXTO);
        p("RadioButton.background", FUNDO_PAINEL);
        p("RadioButton.foreground", TEXTO);
        p("ToggleButton.background", new Color(0x33, 0x33, 0x33));
        p("ToggleButton.foreground", TEXTO);

        // divisor de split pane
        p("SplitPane.background", FUNDO_PAINEL);
        p("SplitPane.dividerFocusColor", BORDA);

        // separadores e outros
        p("Separator.background", BORDA);
        p("Separator.foreground", BORDA);
        p("Desktop.background", FUNDO);
        p("EditorPane.selectionBackground", SELECAO);
        p("TextPane.selectionBackground", SELECAO);
        p("TextArea.selectionBackground", SELECAO);
    }

    private static void p(Object chave, Object valor) {
        UIManager.put(chave, valor);
    }
}