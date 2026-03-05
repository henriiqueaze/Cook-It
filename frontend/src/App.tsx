import { AppRoutes } from "./routes";
import { AuthProvider } from "./contexts/AuthContext";
import { Toaster } from "sonner";

function App() {
  return (
    <AuthProvider>
      <AppRoutes />
      <Toaster position="top-center" richColors />
    </AuthProvider>
  );
}

export default App;
