{
  description = "Nix Tooling for IslesExtra-Fabric";
  inputs.nixpkgs.url = "github:nixos/nixpkgs?ref=nixos-unstable";

  outputs = { self, nixpkgs }:
  let 
    system = "x86_64-linux";
    pkgs = import nixpkgs { inherit system; };
  in
  {
    apps.${system} = {
      default = self.apps.${system}.runClient;
      runClient = {
        type = "app";
        program = "${pkgs.writeShellScriptBin "run-client" ''
          export LD_LIBRARY_PATH=${pkgs.libGL}/lib:$LD_LIBRARY_PATH
          ${pkgs.gradle_9}/bin/gradle runClient -Ddevauth.enabled=true -Ddevauth.configDir="$HOME/.config/devauth"
        ''}/bin/run-client";
      };
    };

    devShells.${system} = {
      default = pkgs.mkShell {
        buildInputs = with pkgs; [
          libGL
          xorg.libX11
          xorg.libXcursor
          xorg.libXrandr
          xorg.libXi
          libxkbcommon
        ];

        shellHook = ''
          export LD_LIBRARY_PATH="${pkgs.lib.makeLibraryPath (with pkgs; [
            libGL
            xorg.libX11
            xorg.libXcursor
            xorg.libXrandr
            xorg.libXi
            libxkbcommon
          ])}:/run/opengl-driver/lib:$LD_LIBRARY_PATH"
        '';
      };
    };
  };
}
