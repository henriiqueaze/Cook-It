import { AppRoutes } from "./routes";
import { AuthProvider } from "./contexts/AuthContext";
import { FavoritesProvider } from "./contexts/FavoritesContext";
import { Toaster } from "sonner";

function App() {
  return (
    <AuthProvider>
      <FavoritesProvider>
        <AppRoutes />
        <Toaster position="top-center" richColors />
      </FavoritesProvider>
    </AuthProvider>
  );
}

export default App;
