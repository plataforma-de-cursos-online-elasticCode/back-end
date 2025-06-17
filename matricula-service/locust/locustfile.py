from locust import HttpUser, task, between
import random
from datetime import date

class MatriculaUser(HttpUser):
    wait_time = between(1, 3)

    def on_start(self):
        self.matricula_id = None
        self.aluno_id = random.randint(1, 10)
        self.curso_id = random.randint(1, 10)

        self.nova_matricula_payload = {
            "alunoId": random.randint(1, 10),
            "cursoId": random.randint(1, 10)
        }

        response = self.client.post("/matriculas", json=self.nova_matricula_payload)
        if response.status_code == 201:
            self.matricula_id = response.json().get("id")
        else:
            self.matricula_id = 1

    @task(3)
    def cadastrar_matricula(self):
        payload = {
            "alunoId": random.randint(1, 10),
            "cursoId": random.randint(1, 10)
        }
        self.client.post("/matriculas", json=payload)

    @task(5)
    def listar_cursos_de_aluno(self):
        self.client.get(f"/matriculas/listar/aluno/{self.aluno_id}")

    @task(3)
    def listar_alunos_de_curso(self):
        self.client.get(f"/produtos/listar/curso/{self.curso_id}")