// src/Dashboard.jsx
import React, { useState, useEffect } from 'react';

export default function Dashboard() {
  const [dados, setDados] = useState(null);
  const [erro, setErro] = useState(null);
  const [carregando, setCarregando] = useState(true);

  useEffect(() => {
    fetch('http://localhost:8080/api/dashboard/resumo')
      .then((response) => {
        if (!response.ok) throw new Error('Falha ao conectar com o servidor StarAge');
        return response.json();
      })
      .then((data) => {
        setDados(data);
        setCarregando(false);
      })
      .catch((err) => {
        setErro(err.message);
        setCarregando(false);
      });
  }, []);

  if (carregando) return <div className="flex justify-center items-center h-screen text-slate-400">Sincronizando telemetria lunar...</div>;
  if (erro) return <div className="text-red-500 text-center mt-10">Erro crítico: {erro}</div>;

  return (
    <div className="min-h-screen bg-slate-900 text-slate-100 p-8">
      <header className="mb-8 border-b border-slate-700 pb-4">
        <h1 className="text-3xl font-bold tracking-wider text-blue-400">STARAGE <span className="text-sm text-slate-500 font-normal">| SISTEMA DE LOGÍSTICA TERRA-LUA</span></h1>
      </header>

      {/* KPIs */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        <div className="bg-slate-800 p-6 rounded-xl border-l-4 border-red-500 shadow-lg">
          <h2 className="text-slate-400 uppercase text-xs tracking-widest">Recursos Críticos</h2>
          <p className="text-4xl font-bold mt-2">{dados.totalProdutosCriticos}</p>
        </div>
        <div className="bg-slate-800 p-6 rounded-xl border-l-4 border-yellow-500 shadow-lg">
          <h2 className="text-slate-400 uppercase text-xs tracking-widest">Itens em Atenção</h2>
          <p className="text-4xl font-bold mt-2">{dados.totalProdutosAtencao}</p>
        </div>
        <div className="bg-slate-800 p-6 rounded-xl border-l-4 border-emerald-500 shadow-lg">
          <h2 className="text-slate-400 uppercase text-xs tracking-widest">Hub Mais Ativo</h2>
          <p className="text-xl font-semibold mt-2">{dados.nomeArmazemMaisAtivo}</p>
        </div>
      </div>

      {/* Tabela de Alertas */}
      <div className="bg-slate-800 rounded-xl p-6 shadow-lg border border-slate-700">
        <h2 className="text-xl font-semibold mb-4 text-slate-200">Alertas de Suporte à Vida</h2>
        <div className="overflow-x-auto">
          <table className="w-full text-left">
            <thead>
              <tr className="border-b border-slate-700 text-slate-400 text-sm uppercase">
                <th className="py-3">Produto</th>
                <th className="py-3">Armazém</th>
                <th className="py-3">Status</th>
                <th className="py-3 text-right">Esgotamento (Dias)</th>
              </tr>
            </thead>
            <tbody>
              {dados.estoqueGeral.map((item) => (
                <tr key={item.idProduto} className="border-b border-slate-700 hover:bg-slate-750">
                  <td className="py-4 font-medium">{item.nomeProduto}</td>
                  <td className="py-4 text-slate-400">{item.nomeArmazem}</td>
                  <td className="py-4">
                    <span className={`px-2 py-1 rounded text-xs font-bold ${
                        item.nivelAlerta === 'CRITICO' ? 'bg-red-900 text-red-200' :
                        item.nivelAlerta === 'ATENCAO' ? 'bg-yellow-900 text-yellow-200' :
                        'bg-green-900 text-green-200'
                    }`}>
                      {item.nivelAlerta}
                    </span>
                  </td>
                  <td className="py-4 text-right font-mono">{item.diasRestantes} dias</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}