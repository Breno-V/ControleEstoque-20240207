# 📦	Aplicação de Controle de Estoque (Java 21 + SpringBoot)
## 🔖	Objetivo
O **Objetivo** desse projeto foi **aprimorar minha habilidade** com a linguagem de programação **Java** e descobrir mais sobre como funciona o framework **SpringBoot** desenvolvendo uma API para um Controle de Estoque fictício.
## 🖥️	 Tecnologias
<img src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/java/java-original.svg" width="100"/><img src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/spring/spring-original.svg" width="90"/>
## 🎬	Demonstração
---

---
## 📌	Como testar a API
### 1. Dependências necessárias
1. [Java 21](https://www.oracle.com/java/technologies/downloads/#java21) instalado em sua máquina
2. [VS Code](https://code.visualstudio.com) (ou qualquer outro editor de texto) para conseguir clonar meu repositório
3. [Xampp](https://www.apachefriends.org/pt_br/download.html) para conseguir visualizar os dados sendo armazenados/alterados
> 4.  [Maven](https://maven.apache.org/download.cgi) será somente necessário se você não utiliza o VS Code, que disponibiliza extensões para facilitar a execução e compilação do código

### 2. Clonando o repositório
1. Copie o comando pra clonar meu repositório e cole-o no terminal do seu editor de texto:
```
git clone https://github.com/Breno-V/ControleEstoque-20240207
```
### 3. Instalando o Xampp
O Xampp será uma ferramenta útil e necessária para conseguir sincronizar minha aplicação com o MySQL, que é o banco de dados que eu utilizo.
Logo a seguir, disponibilizo um vídeo rápido de tutorial para conseguir instalar corretamente o xampp:
[Como instalar o Xampp no Windows 11](https://youtu.be/9LhQfvqc09M?si=E0luczGI0Ob_8KwA)
> Caso esteja usando Linux, recomendo pesquisar algum outro tutorial de acordo com sua distro. Além disso, pode utilizar de outro serviço fora o xampp, contanto que tenha MySQL disponível

**Após a instalação do Xampp, aperte em *Start* no Apache e no MySQL**

---
### 4. Executando o Projeto
Para quem utiliza o **VS Code**, siga esses passos:
#### 1. Baixe as extensões necessárias
Para facilitar a execução e compilação do código, a Microsoft disponibilizou um pacote de extensões exclusivo para aplicações Java, apelidado de **Extension Pack for Java** que contém as seguintes extensões:
- Language Support for Java (Red Hat)
- Debbuger for Java (Microsoft)
- Test Runner for Java (Microsoft)
- Maven for Java (Microsoft)
- Gradle for Java (Microsoft)
- Project Manager for Java (Microsoft)

#### 2. Rode o Projeto
O último passo é apenas entrar dentro do arquivo **ApiEstoqueApplication.java** que está em: ```src/main/java/com/controleestoque/api_estoque/ApiEstoqueApplication.java``` e apertar no botão de *Play* que está presente no canto superior direito.

---
Para quem utiliza algum outro editor de texto, é necessário a instalação do Maven. ([Como instalar o Maven](https://youtu.be/rfhTnfbBQcY?si=0l0eQGPCHhNUQK6z)) <br>Após a instalação, siga estes passos:
#### 1. Adentre-se na pasta raiz do projeto
Abra um terminal dentro da pasta do projeto — é a mesma pasta onde está localizado o arquivo `pom.xml`.<br><br>
Exemplo no **Windows**:
```cd C:\Users\seuUsuario\ControleEstoque-20240207```<br><br>
Exemplo no **Linux**:
```cd ~/ControleEstoque-20240207```<br>
#### 2. Verifique se o Maven foi instalado corretamente
No terminal, digite:
```
mvn --v
```
Se aparecer algo como:
```
Apache Maven 3.x.x 
Java version: 21
```
Está tudo correto!
#### 3. Instale as dependências do projeto
Ainda dentro da pasta raiz, execute:
```
mvn clean install
```
Este comando faz com que:
- Baixe todas as dependências declaradas no `pom.xml`
- Compile o projeto
- Prepare tudo para execução
---
#### 4. Execute a aplicação
Agora você tem **duas opções**:

##### Opção 1 — Rodar diretamente com o Maven:
```
mvn spring-boot:run
```
Resultado:

- A aplicação inicia
- O Spring Boot sobe o servidor
- A API começa a responder em:  
  **http://localhost:8080**

---
##### Opção 2 — Gerar e rodar o arquivo `.jar`
Primeiro gere o JAR:
```
mvn clean package -DskipTests
```
Após isso, entre na pasta `target`:
```
cd target
```
E execute o `.jar` gerado:
```
java -jar controle-estoque-0.0.1-SNAPSHOT.jar
```
**Pronto** — sua API estará rodando da mesma forma:
---
