import { useEffect, useMemo, useState } from "react";
import { Ban, MessageSquare, Users, UtensilsCrossed } from "lucide-react";
import type { LucideIcon } from "lucide-react";
import { adminService } from "@/services/adminService";

type Accent = "orange" | "emerald" | "sky" | "red";

function accentClasses(accent: Accent) {
  const classes = {
    orange: "bg-orange-50 text-orange-600",
    emerald: "bg-emerald-50 text-emerald-600",
    sky: "bg-sky-50 text-sky-600",
    red: "bg-red-50 text-red-600",
  };

  return classes[accent];
}

function Metric({
  label,
  value,
  icon: Icon,
  accent = "orange",
}: {
  label: string;
  value: number;
  icon: LucideIcon;
  accent?: Accent;
}) {
  return (
    <div className="rounded-2xl bg-white p-5 shadow-lg shadow-gray-200/60">
      <div className="flex items-start justify-between gap-3">
        <div>
          <p className="text-sm font-medium text-gray-500">{label}</p>
          <div className="mt-3 text-3xl font-black text-gray-900">{value}</div>
        </div>
        <div className={`rounded-xl p-2.5 ${accentClasses(accent)}`}>
          <Icon size={19} />
        </div>
      </div>
    </div>
  );
}

export function AdminOverview() {
  const [summary, setSummary] = useState({
    totalUsers: 0,
    adminUsers: 0,
    bannedUsers: 0,
    totalRecipes: 0,
    totalIngredients: 0,
    totalComments: 0,
    bannedWords: 0,
  });

  useEffect(() => {
    adminService.resumo().then(setSummary);
  }, []);

  const moderationTotal = useMemo(
    () => summary.bannedUsers + summary.bannedWords,
    [summary.bannedUsers, summary.bannedWords],
  );
  const contentTotal = summary.totalRecipes + summary.totalComments;

  return (
    <div className="space-y-5">
      <section className="overflow-hidden rounded-2xl bg-white shadow-lg shadow-gray-200/60">
        <div className="bg-linear-to-br from-orange-500 via-orange-600 to-red-600 px-5 py-6 text-white sm:px-6">
          <div className="flex flex-col gap-5 text-center md:flex-row md:items-end md:justify-between md:text-left">
            <div>
              <h2 className="text-2xl font-black tracking-tight sm:text-3xl">
                Visão geral
              </h2>
              <p className="mt-2 max-w-2xl text-sm text-orange-100">
                Acompanhe usuários, conteúdo publicado e regras de moderação em
                um só lugar.
              </p>
            </div>

            <div className="grid grid-cols-2 gap-3 sm:min-w-72">
              <div className="rounded-2xl bg-white/15 p-4">
                <div className="text-2xl font-black">{contentTotal}</div>
                <div className="mt-1 text-xs text-orange-100">conteúdos</div>
              </div>
              <div className="rounded-2xl bg-white/15 p-4">
                <div className="text-2xl font-black">{moderationTotal}</div>
                <div className="mt-1 text-xs text-orange-100">
                  regras/ações
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      <section className="grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
        <Metric label="Usuários" value={summary.totalUsers} icon={Users} />
        <Metric
          label="Receitas"
          value={summary.totalRecipes}
          icon={UtensilsCrossed}
          accent="emerald"
        />
        <Metric
          label="Comentários"
          value={summary.totalComments}
          icon={MessageSquare}
          accent="sky"
        />
        <Metric
          label="Palavras banidas"
          value={summary.bannedWords}
          icon={Ban}
          accent="red"
        />
      </section>
    </div>
  );
}
