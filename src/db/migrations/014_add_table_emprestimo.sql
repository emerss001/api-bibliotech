drop table Bibliotech.Emprestimo;

create table Emprestimo (
    id  int auto_increment primary key,
    material_id             int not null,
    aluno_id                int not null,
    data_emprestimo         datetime default current_timestamp,
    data_devolucao_prevista datetime generated always as (date_add(data_emprestimo, interval 7 day)),
    data_devolucao_real     datetime null,
    status                  enum ('Aprovado', 'Pendente', 'Devolvido', 'Renovado', 'Rejeitado') default 'Pendente' not null,
    foreign key (material_id) references Material (id),
    foreign key (aluno_id) references Aluno (pessoa_id)
);