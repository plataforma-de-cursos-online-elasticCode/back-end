# 🎓 Plataforma de Cursos Online

Sistema modular baseado em microserviços para criação, gestão e consumo de cursos online, desenvolvido com Java 17 e Spring Boot. A plataforma permite que professores ofereçam cursos e que alunos se inscrevam, acessem conteúdos, emitam certificados, avaliem cursos e simulem pagamentos.

## 📚 Visão Geral

João, um professor empreendedor, idealizou essa plataforma para vender seus cursos e permitir que outros educadores façam o mesmo. Com arquitetura moderna e escalável, o sistema é dividido em microserviços independentes e de fácil manutenção.

---

## 🧩 Arquitetura de Microserviços

| Serviço                | Responsabilidades                                                                 |
|------------------------|------------------------------------------------------------------------------------|
| `usuario-service`      | Registro, login, controle de usuários (aluno/professor), geração e validação JWT. |
| `curso-service`        | CRUD de cursos por professores, listagem e busca de cursos.                        |
| `matricula-service`    | Matrícula de alunos em cursos, controle e listagem de matrículas.                 |
| `conteudo-service`     | Gerenciamento de aulas (vídeos e PDFs) e controle de acesso ao conteúdo.          |
| `gateway-service`      | Roteamento de chamadas, autenticação JWT e controle de permissões.                |
| `certificado-service`  | Geração de certificados de conclusão com base nas matrículas e progresso.         |
| `avaliacao-service`    | Sistema de avaliações e comentários de cursos por alunos.                         |
| `pagamento-service`    | Simulação de pagamentos para matrícula em cursos pagos.                           |

---

## ⚙️ Funcionalidades Principais

### 👤 Serviço de Usuários (`usuario-service`)
- Cadastro e login de alunos e professores.
- Geração e validação de tokens JWT para autenticação.
- Armazena nome, e-mail, senha e tipo de usuário.

### 📘 Serviço de Cursos (`curso-service`)
- Professores podem criar, editar e listar cursos.
- Busca por nome ou categoria.
- Cursos contêm descrição, preço e categoria.

### 🎓 Serviço de Matrículas (`matricula-service`)
- Alunos podem se matricular em cursos (gratuitos ou pagos).
- Registro da data da matrícula.
- Listagem de cursos por aluno e alunos por curso.

### 📺 Serviço de Conteúdo (`conteudo-service`)
- Cada curso possui aulas com vídeo e materiais em PDF.
- Acesso restrito a alunos matriculados.

### 🛡️ API Gateway (`gateway-service`)
- Roteamento de requisições para os microserviços.
- Proteção de endpoints com validação JWT.
- Permissões específicas (ex: apenas professores criam cursos).

### 🏆 Serviço de Certificados (`certificado-service`)
- Geração automática de certificados de conclusão.
- Verificação se o aluno concluiu todas as aulas do curso.
- Pode integrar com MailHog ou RabbitMQ para envio por e-mail.

### 🌟 Serviço de Avaliações (`avaliacao-service`)
- Alunos podem avaliar cursos com notas e comentários.
- Média de avaliações visível nos detalhes do curso.
- Professores podem visualizar feedbacks dos alunos.

### 💳 Serviço de Pagamentos (`pagamento-service`)
- Simulação de pagamentos para matrícula em cursos pagos.
- Geração de status de pagamento (simulado: aprovado, recusado).
- Integração com matrícula para liberar acesso após pagamento.

---

## 📏 Regras de Negócio

- Apenas usuários autenticados podem acessar conteúdos e recursos.
- Professores só podem editar seus próprios cursos.
- Alunos só acessam conteúdo dos cursos em que estão matriculados.
- Certificados só são gerados após conclusão do curso.
- Cursos pagos requerem simulação de pagamento antes da matrícula.
- Alunos podem avaliar apenas os cursos concluídos.

---

## 🚀 Possíveis Extensões Futuras

- Upload real de vídeos e PDFs (S3 ou equivalente).
- Envio de e-mail com certificado e recibo de pagamento.
- Painel administrativo para professores.
- Notificações em tempo real (ex: conclusão de curso).

---

## 🛠️ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot**
- **Spring Security + JWT**
- **Spring Data JPA**
- **WebClient (comunicação entre serviços)**
- **MySQL** ou **PostgreSQL**
- **Maven**
- **Docker** (opcional)
- **RabbitMQ / MailHog** (para certificados)
- **Lombok**, **MapStruct**, **OpenAPI/Swagger** (opcional)

---

## 📦 Como Executar Localmente

### Pré-requisitos:
- Java 17+
- Maven
- MySQL/PostgreSQL
- Docker (opcional)

### Passos:
1. Clone o repositório:
   ```bash
   git clone https://github.com/plataforma-de-cursos-online-elasticCode/back-end
   cd back-end

2. Compile os serviços individualmente
   ```bash
   cd usuario-service
   mvn clean install

   cd ../curso-service
   mvn clean install

   cd ../matricula-service
   mvn clean install

   cd ../conteudo-service
   mvn clean install

   cd ../certificado-service
   mvn clean install

   cd ../avaliacao-service
   mvn clean install

   cd ../pagamento-service
   mvn clean install

   cd ../gateway-service
   mvn clean install

