package Telas;

/**
 * Callback para as ações da {@link IdePanel} (botões da barra de
 * ferramentas). Estende {@link SidebarListener} pelos mesmos motivos
 * de {@link HomePanelListener}.
 *
 * @author Nicolas Gabriel Venâncio dos Santos
 * @version 1.0
 */
public interface IdePanelListener extends SidebarListener {

    /** Executa toda a consulta do editor ativo (F5). */
    void aoExecutar(String sql);

    /** Executa só a instrução onde o cursor está (F6). */
    void aoExecutarUnica(String sql);

    /** Interrompe a execução em andamento. */
    void aoParar();

    /** Salva o conteúdo do editor ativo. */
    void aoSalvar();

    /** Abre o histórico de execuções. */
    void aoAbrirHistorico();
}