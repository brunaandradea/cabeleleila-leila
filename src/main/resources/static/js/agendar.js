const usuarioId = localStorage.getItem('usuarioId');
const usuarioNome = localStorage.getItem('usuarioNome');

if (!usuarioId) {
    window.location.href = 'index.html';
}

const nomeClienteEl = document.getElementById('nome-cliente');
if (nomeClienteEl) nomeClienteEl.textContent = usuarioNome || 'Cliente';

async function carregarServicos() {
    const container = document.getElementById('container-servicos');
    const erro = document.getElementById('mensagem-erro');
    if (!container) return;

    try {
        const servicos = await apiGet('/servicos');
        container.innerHTML = '';

        const ativos = (servicos || []).filter(s => s.ativo !== false);

        if (ativos.length === 0) {
            container.innerHTML = '<p class="empty-state">Nenhum serviço disponível no momento.</p>';
            return;
        }

        ativos.forEach(servico => {
            const div = document.createElement('div');
            div.className = 'servico-item';

            const label = document.createElement('label');
            label.className = 'servico-label';

            const info = document.createElement('span');
            info.innerHTML = `<b>${servico.nome}</b> - R$ ${Number(servico.precoAtual || 0).toFixed(2)} (${servico.duracaoMinutos} min)`;

            const checkbox = document.createElement('input');
            checkbox.type = 'checkbox';
            checkbox.value = servico.id;
            checkbox.className = 'chk-servico';

            label.appendChild(info);
            label.appendChild(checkbox);
            div.appendChild(label);
            container.appendChild(div);
        });
    } catch (err) {
        container.innerHTML = '<p class="empty-state">Não foi possível carregar os serviços.</p>';
        if (erro) erro.textContent = 'Falha ao carregar os serviços. Tente novamente.';
        console.error(err);
    }
}

async function realizarAgendamento() {
    const erro = document.getElementById('mensagem-erro');
    if (erro) erro.textContent = '';

    const servicos = document.querySelectorAll('.chk-servico:checked');
    const servicoIds = Array.from(servicos).map(cb => cb.value);
    const dataHoraInput = document.getElementById('data-hora')?.value;

    if (servicoIds.length === 0) {
        if (erro) erro.textContent = 'Selecione ao menos um serviço.';
        return;
    }

    if (!dataHoraInput) {
        if (erro) erro.textContent = 'Selecione uma data e hora.';
        return;
    }

    const minutos = new Date(dataHoraInput).getMinutes();
    if (minutos !== 0 && minutos !== 30) {
        if (erro) erro.textContent = 'Selecione um horário em ponto ou meia hora.';
        return;
    }

    const dataSelecionada = new Date(dataHoraInput);
    const diaDaSemana = dataSelecionada.getDay();
    const hora = dataSelecionada.getHours();
    if (diaDaSemana === 0 || diaDaSemana === 1) {
        if (erro) erro.textContent = 'Agendamentos só podem ser feitos de terça a sábado.';
        return;
    }
    if (hora < 8 || hora >= 18) {
        if (erro) erro.textContent = 'Agendamentos só podem ser feitos entre 08:00 e 18:00.';
        return;
    }

    const dataHoraIso = dataHoraInput.length === 16 ? `${dataHoraInput}:00` : dataHoraInput;

    try {
        const sugestao = await apiGet(`/agendamentos/sugestao-data?usuarioId=${usuarioId}&dataDesejada=${encodeURIComponent(dataHoraIso)}`);

        if (sugestao) {
            const dt = new Date(sugestao);
            const dataFmt = dt.toLocaleDateString('pt-BR');
            const horaFmt = dt.toLocaleTimeString('pt-BR', {hour: '2-digit', minute: '2-digit'});

            const aceita = confirm(
                `Já existe um agendamento na semana para ${dataFmt} às ${horaFmt}.` +
                `\n\nDeseja manter esta sugestão?`
            );

            if (!aceita) return;
        }

        await apiPost('/agendamentos', {
            usuarioId,
            servicoIds,
            dataHora: dataHoraIso,
            observacao: document.getElementById('observacao')?.value || ''
        });

        alert('Agendamento realizado com sucesso!');
        window.location.href = 'historico.html';
    } catch (err) {
        if (erro) erro.textContent = `Erro ao agendar: ${err.message}`;
        console.error(err);
    }
}

document.getElementById('btn-agendar')?.addEventListener('click', realizarAgendamento);
carregarServicos();