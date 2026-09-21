package Telas;

/**
 * Callback para os eventos de clique da {@link HomePanel}.
 *
 * A HomePanel nao sabe nada sobre navegacao real (troca de telas,
 * abertura de dialogos, etc.) -- ela so avisa QUAL elemento foi
 * clicado, e quem implementar esta interface decide O QUE fazer.
 * Normalmente isso significa trocar o card ativo de um CardLayout na
 * janela principal (FramePortuSQL).
 *
 * <p>Estende {@link SidebarListener} porque a barra lateral e
 * compartilhada entre as telas: os tres icones (inicio, banco,
 * configuracoes) sao os mesmos da Homepage e do editor de consultas.
 *
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 */
public interface HomePanelListener extends SidebarListener {

    /** Chamado ao clicar no card "Novo banco". */
    void aoClicarNovoBanco();

    /** Chamado ao clicar no card "Sugestoes". */
    void aoClicarSugestoes();

    /** Chamado ao clicar no card "Duvidas". */
    void aoClicarDuvidas();

    /** Chamado ao clicar no card "Comandos". */
    void aoClicarComandos();

    /** Chamado ao clicar no card "Quem somos". */
    void aoClicarQuemSomos();
}