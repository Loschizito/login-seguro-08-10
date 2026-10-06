# Login Seguro

Sistema de login com cadastro, autenticação e autorização por perfil, feito com Java Spring Boot, Spring Security, Thymeleaf e MongoDB Atlas.

## Tecnologias

- Java 21
- Spring Boot 3.3
- Spring Security (senha com BCrypt)
- Spring Data MongoDB
- Spring Session MongoDB (sessões salvas no banco)
- Thymeleaf
- Maven

## Perfis

| Perfil    | Acesso                                   |
|-----------|------------------------------------------|
| USUARIO   | `/painel`                                |
| MODERADOR | `/painel`, `/moderador`                  |
| ADMIN     | `/painel`, `/moderador`, `/admin`        |

Todo usuário novo é cadastrado como `USUARIO`. O admin pode mudar o perfil de qualquer usuário na página `/admin`.

Na primeira execução é criado um usuário ADMIN com o e-mail e a senha definidos no `.env` (`ADMIN_EMAIL` e `ADMIN_SENHA`).

## Estrutura

```
src/main/java/com/loginseguro
├── config        SecurityConfig, TemaConfig, AdminInicial
├── controller    AuthController, PainelController
├── dto           CadastroDto
├── model         Usuario, Role
├── repository    UsuarioRepository
└── service       UsuarioService

src/main/resources
├── application.properties
├── templates     páginas Thymeleaf (layout.html tem os fragmentos comuns)
└── static/temas  um CSS por tema (padrao, escuro)
```

## Configurando o MongoDB Atlas

1. Crie uma conta em https://www.mongodb.com/atlas e crie um cluster gratuito (M0).
2. Em **Database Access**, crie um usuário com senha.
3. Em **Network Access**, libere o seu IP (ou `0.0.0.0/0` só para testes).
4. Em **Connect > Drivers**, copie a connection string (`mongodb+srv://...`).

A conexão `mongodb+srv` usa TLS por padrão, então os dados trafegam criptografados.

O sistema cria sozinho duas coleções no banco:

- `usuarios`: dados dos usuários (com índice único no e-mail)
- `sessoes`: sessões de login (Spring Session)

## Arquivo de configuração

Copie o arquivo de exemplo e preencha com os seus dados:

```bash
cp .env.example .env
```

```properties
MONGODB_URI=mongodb+srv://USUARIO:SENHA@CLUSTER.mongodb.net/?retryWrites=true&w=majority&tls=true
MONGODB_DATABASE=loginseguro
ADMIN_EMAIL=admin@loginseguro.com
ADMIN_SENHA=TroqueEstaSenha123
APP_TEMA=padrao
```

O `.env` está no `.gitignore`, então a senha do banco não vai para o GitHub. O `application.properties` lê esse arquivo com `spring.config.import`.

## Executando localmente

Pré-requisitos: Java 21 e Maven instalados.

```bash
git clone https://github.com/SEU_USUARIO/login-seguro.git
cd login-seguro
cp .env.example .env
mvn spring-boot:run
```

Acesse http://localhost:8080

Para gerar o `.jar`:

```bash
mvn clean package
java -jar target/login-seguro-1.0.0.jar
```

## Temas

O tema é escolhido pela variável `APP_TEMA` no `.env`. Já existem `padrao` e `escuro`.

Para criar um tema novo, crie a pasta `src/main/resources/static/temas/NOME/` com um `style.css` e coloque `APP_TEMA=NOME`. As páginas não mudam, porque todas usam o `layout.html` e só o CSS é trocado.

## Gitflow

- `main`: versão entregue
- `develop`: desenvolvimento
- `feature/*`: cada funcionalidade
- `release/*`: preparação da versão

Para publicar no GitHub:

```bash
git remote add origin https://github.com/SEU_USUARIO/login-seguro.git
git push -u origin main develop --tags
git push origin --all
```
