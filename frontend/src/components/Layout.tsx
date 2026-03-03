import { Outlet } from "react-router-dom";
import { BottomNav } from "./BottomNav";

export function Layout() {
  return (
    <div className="max-w-md mx-auto min-h-screen bg-gray-50 relative">
      <main className="pb-16">
        <Outlet />
      </main>
      <BottomNav />
    </div>
  );
}
