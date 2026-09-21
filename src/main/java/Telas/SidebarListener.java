package Telas;

/**
 * Callback para os 3 ícones da barra lateral (banco, início,
 * configurações). Separado de {@link HomePanelListener} porque a
 * barra lateral agora é reaproveitada em mais de uma tela (Homepage e
 * o editor de consultas) -- cada tela implementa só isto, não os
 * métodos específicos dos cards da Homepage.
 *
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 */
public interface SidebarListener {

    /** Chamado ao clicar no ícone de banco de dados. */
    void aoClicarBancos();

    /** Chamado ao clicar no ícone de início. */
    void aoClicarInicio();

    /** Chamado ao clicar no ícone de configurações. */
    void aoClicarConfiguracoes();
}