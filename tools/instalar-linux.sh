#!/bin/sh
#
# PortuSQL - instalador para Linux (MATE, GNOME, XFCE, Cinnamon...).
#
# Instala o executavel no diretorio de dados do usuario, cria o icone e
# registra um atalho no menu de aplicacoes, de modo que o PortuSQL possa
# ser aberto com dois cliques, como qualquer outro programa.
#
# Uso:
#   ./instalar-linux.sh [caminho/para/PortuSQL.jar]
#
# Sem argumento, usa o PortuSQL.jar que estiver ao lado do proprio script.

set -eu

RAIZ="$HOME/.local/share"
DESTINO="$RAIZ/portusql"
APLICACOES="$RAIZ/applications"
ICONES="$RAIZ/icons"

# ---------------------------------------------------------------- origem ---
AQUI=$(cd "$(dirname "$0")" && pwd)
if [ $# -ge 1 ]; then
    JAR="$1"
else
    JAR=""
    for candidato in "$AQUI/PortuSQL.jar" "$AQUI/PortuSQL-1.0.0.jar" "$AQUI/../PortuSQL.jar"; do
        [ -f "$candidato" ] && JAR="$candidato" && break
    done
fi

if [ -z "$JAR" ] || [ ! -f "$JAR" ]; then
    echo "ERRO: nao encontrei o PortuSQL.jar."
    echo "Use:  ./instalar-linux.sh /caminho/para/PortuSQL.jar"
    exit 1
fi

case "$JAR" in
    /*) ;;
    *) JAR="$(cd "$(dirname "$JAR")" && pwd)/$(basename "$JAR")" ;;
esac

echo "Origem:  $JAR"

# ------------------------------------------------------------- instalacao ---
mkdir -p "$DESTINO" "$APLICACOES" "$ICONES"
cp -f "$JAR" "$DESTINO/PortuSQL.jar"
chmod +x "$DESTINO/PortuSQL.jar"
echo "Instalado em: $DESTINO/PortuSQL.jar"

# O icone esta dentro do proprio jar (src/main/resources/portusql.ico).
# O 'unzip' avisa (e sai com codigo != 0) sobre os bytes do prefixo de
# shell, mas extrai o conteudo normalmente -- por isso o teste olha o
# arquivo gerado, e nao o codigo de saida do unzip.
if command -v unzip >/dev/null 2>&1; then
    unzip -p "$DESTINO/PortuSQL.jar" portusql.ico > "$ICONES/portusql.ico" 2>/dev/null || true
    if [ -s "$ICONES/portusql.ico" ]; then
        echo "Icone:    $ICONES/portusql.ico"
    else
        rm -f "$ICONES/portusql.ico"
        echo "Icone:    nao foi possivel extrair do jar (continua sem icone)"
    fi
else
    echo "Icone:    'unzip' nao instalado (continua sem icone)"
fi

cat > "$APLICACOES/portusql.desktop" <<EOF
[Desktop Entry]
Type=Application
Version=1.0
Name=PortuSQL
GenericName=Editor SQL
Comment=SQL em portugues para o ensino de bancos de dados
Exec=java -jar "$DESTINO/PortuSQL.jar"
TryExec=java
Icon=$ICONES/portusql.ico
Terminal=false
Categories=Development;Database;Education;
Keywords=SQL;banco de dados;H2;PortuSQL;educacao;
StartupNotify=true
EOF

chmod +x "$APLICACOES/portusql.desktop"
echo "Atalho:  $APLICACOES/portusql.desktop"

# O MATE/GNOME so leem o atalho depois de uma varredura do cache.
if command -v update-desktop-database >/dev/null 2>&1; then
    update-desktop-database "$APLICACOES" >/dev/null 2>&1 || true
fi

# ------------------------------------------------------------------ final ---
echo
echo "Instalacao concluida."
echo
echo "  - Para abrir: procure 'PortuSQL' no menu de aplicacoes"
echo "  - Ou direto:  java -jar \"$DESTINO/PortuSQL.jar\""
echo
echo "Desinstalar:  rm -f \"$DESTINO/PortuSQL.jar\" \"$APLICACOES/portusql.desktop\" \"$ICONES/portusql.ico\""
