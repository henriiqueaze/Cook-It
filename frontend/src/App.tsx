import { Toaster } from "sonner";
import { AuthProvider } from "./contexts/AuthContext";
import { FavoritesProvider } from "./contexts/FavoritesContext";
import { AppRoutes } from "./routes";

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
