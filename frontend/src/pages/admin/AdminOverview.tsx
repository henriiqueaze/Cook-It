import { useEffect, useState } from "react";
import {
  Ban,
  MessageSquare,
  Shield,
  Users,
  UtensilsCrossed,
} from "lucide-react";
import type { LucideIcon } from "lucide-react";
import { adminService } from "@/services/adminService";

function Metric({
  label,
  value,
  icon: Icon,
}: {
  label: string;
  value: number;
  icon: LucideIcon;
}) {
  return (
    <div className="rounded-2xl border border-white/70 bg-white p-5 shadow-lg shadow-orange-100/40">
      <div className="flex items-center justify-between">
        <span className="text-sm font-medium text-gray-500">{label}</span>
        <div className="rounded-xl bg-orange-50 p-2 text-orange-600">
          <Icon size={18} />
        </div>
      </div>
      <div className="mt-4 text-3xl font-black text-gray-900">{value}</div>
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

  return (
    <section className="rounded-3xl border border-orange-100 bg-white/90 p-6 shadow-xl shadow-orange-100/60 backdrop-blur">
      <div className="mb-6">
        <h1 className="text-3xl font-black tracking-tight text-gray-900">
          Admin
        </h1>
        <p className="mt-2 max-w-2xl text-sm text-gray-600">
          Moderação e controle centralizado.
        </p>
      </div>

      <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-4">
        <Metric label="Usuários" value={summary.totalUsers} icon={Users} />
        <Metric
          label="Receitas"
          value={summary.totalRecipes}
          icon={UtensilsCrossed}
        />
        <Metric
          label="Comentários"
          value={summary.totalComments}
          icon={MessageSquare}
        />
        <Metric
          label="Palavras banidas"
          value={summary.bannedWords}
          icon={Ban}
        />
        <Metric label="Admins" value={summary.adminUsers} icon={Shield} />
        <Metric
          label="Usuários banidos"
          value={summary.bannedUsers}
          icon={Ban}
        />
      </div>
    </section>
  );
}
