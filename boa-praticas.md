# Guia de Boas Praticas do Projeto

Aqui estão as regras e os combinados do nosso grupo para fazer o trabalho dar certo e ninguem se bater durante o desenvolvimento, desde a escrita do codigo ate a gravacao do video final.

## 1. O Video de Apresentacao (Regras do Professor)

Essa e a parte que vai garantir a nossa nota final, entao precisamos seguir a risca o que foi pedido na Unidade 2:

- Tempo do video: O video precisa ter entre 15 e 20 minutos, sem excecao.
- Todo mundo fala: Como somos 9 pessoas, todos precisam explicar sua parte no video. Quando for a sua vez de falar, deixe bem claro qual foi a sua tarefa (User Story/Task) no Jira.
- Ferramentas de gravacao: O professor recomendou usar o OBS Studio para gravar a tela do computador e o IceCream Video Editor para colar os videos de todo mundo em um so. Ele tambem indicou um curso de videoaulas da Escola Virtual do Bradesco pra quem quiser melhorar a didatica e a iluminacao.
- Atencao com o link do YouTube: Quando formos postar o video final no YouTube, ele NAO PODE ficar como "Privado". Tem que marcar como "Nao Listado" (onde so quem tem o link acessa) ou "Publico". Se ficar privado, o professor nao vai conseguir ver e o grupo fica sem nota.

## 2. Como vamos usar o GitHub

Mexer com 9 pessoas no mesmo codigo e perigoso, entao vamos seguir esse fluxo:

- A branch main e intocavel: Ninguem joga codigo direto na main. Ela e a nossa versao final. Tudo que formos fazer de teste e desenvolvimento vai se encontrar na branch develop.
- Crie sua propria branch: Vai pegar uma tarefa pra fazer? Crie uma branch com o nome da tarefa. Exemplo: feature/menu-de-quartos. Nao programe na branch do coleguinha.
- Revise antes de juntar: Terminou o seu codigo? Abra um Pull Request para a develop e peca para alguem do grupo dar uma lida rapida antes de aceitar.
- Commits que fazem sentido: Nao escreva apenas "atualizei" ou "teste" no commit. Escreva o que voce realmente fez, tipo "criada a classe de hospedes".

## 3. O Codigo e as Estruturas de Dados

Lembrando que o foco da materia e usar as estruturas corretamente e que nao teremos banco de dados real.

- Nao percam os dados: Como tudo fica na memoria (RAM), todas as listas de quartos, filas de espera e pilhas de historico precisam ficar centralizadas em um unico lugar (uma classe principal). Se a tela de cadastro instanciar uma lista nova e a tela de busca instanciar outra, o sistema nao vai achar nada.
- Expliquem o motivo das escolhas: O professor quer saber se entendemos o assunto. Quando criarem a Fila de espera ou a Pilha do historico, coloquem um comentario no codigo explicando por que aquela estrutura foi a melhor escolha pra resolver o problema.
- Nao deixe o programa fechar sozinho: O usuario do sistema vai errar. Se o menu pedir um numero e ele digitar uma letra, o programa nao pode "crashar". Lembrem de usar try/catch nos menus.

## 4. Organizacao no Jira

O Jira e o que vai provar pro professor que todo mundo trabalhou por igual.

- Atualize seus cards: Comecou a fazer o codigo ou o design? Puxe o seu card pra coluna "Em Progresso". Terminou? Puxe pra "Concluido".
- Pontuacao equilibrada: Todas as tarefas tem que ter os pontos de dificuldade (Story Points). A equipe de gestao precisa garantir que no final os 9 membros tenham somado uma quantidade parecida de pontos.
