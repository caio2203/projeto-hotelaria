# Sistema de Gerenciamento de Reservas em Hotel

Esse é o repositório do nosso trabalho prático para a disciplina de Estruturas de Dados. 

O objetivo do projeto é criar um sistema backend em Java para gerenciar um hotel. A principal regra do trabalho é que não estamos usando nenhum banco de dados (como MySQL ou Postgres). Todas as informações ficam salvas na memória principal do programa enquanto ele roda, usando as estruturas de dados que aprendemos na matéria.

## O que o sistema faz

O sistema foca em duas partes principais: o controle do hotel e a visão do cliente.

- Controle de Quartos: Dá para cadastrar quartos (solteiro, casal, suíte), ver o que está disponível, bloquear quarto pra manutenção e calcular a taxa de ocupação do hotel.
- Reservas e Hospedagem: O sistema faz o check-in e check-out, permite cancelar reservas e, se o hotel estiver lotado, coloca o cliente numa lista de espera.
- Histórico: Guarda por onde o cliente já passou e o valor das diárias.

## Como funciona por baixo dos panos

Para fazer o sistema rodar sem banco de dados e de forma eficiente, aplicamos as seguintes estruturas:

- Listas (ArrayList/LinkedList): Usamos para o inventário geral. É onde ficam guardados todos os quartos do hotel e os clientes cadastrados, facilitando a busca na hora de checar datas.
- Filas (Queue): Usamos para a Lista de Espera. Ela segue a regra FIFO (First-In, First-Out), garantindo que o primeiro cliente que não conseguiu vaga seja o primeiro a ser chamado quando alguém cancelar uma reserva.
- Pilhas (Stack): Usamos para o Histórico de Hospedagem de cada cliente. Seguindo a regra LIFO (Last-In, First-Out), a última viagem que o cliente fez fica no topo da pilha, sendo a primeira a aparecer quando consultamos o perfil dele.

## Divisão da Equipe

Como nosso grupo tem 9 pessoas, dividimos o trabalho em frentes para ninguém pisar no código do outro:

- Backend e Infraestrutura (Java e Estruturas): [Nome 1], [Nome 2], [Nome 3]
- Arquitetura e Regras de Negócio (Validações): [Nome 4], [Nome 5]
- UI Design (Telas no Figma): [Nome 6], [Nome 7]
- Documentação, Jira e Vídeo: [Nome 8], [Nome 9]

## Como rodar o projeto

1. Faça o clone deste repositório na sua máquina:
   git clone https://github.com/caio2203/projeto-ed-hotelaria.git
2. Abra a pasta do projeto na sua IDE (IntelliJ, Eclipse, VS Code, etc).
3. Encontre o arquivo principal (Main.java) e rode a aplicação.
4. O sistema vai abrir os menus de interação direto no terminal/console.
