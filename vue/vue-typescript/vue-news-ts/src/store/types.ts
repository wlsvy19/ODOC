import { CommitOptions, Store } from "vuex";
import { Mutations } from "./mutations";
import { RootState } from "./state";

// 커스텀으로 만든 타입

type MyMutations = {
  commit<K extends keyof Mutations, P extends Parameters<Mutations[K]>[1]>(
    key: K,
    payload?: P,
    options?: CommitOptions
  ): ReturnType<Mutations[K]>;
};

// Omit: <범위, 뺄거> -> commit만 뺌 -> commit은 MyMutations에서 정의된거 사용 할래
export type MyStore = Omit<Store<RootState>, "commit"> & MyMutations;
