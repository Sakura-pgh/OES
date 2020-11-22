import subjectApi from "@/api/subject";
import topicApi from "@/api/topic";

const state = {
  subjects: [],
  topics: []
};

const getters = {
  subjectEnumFormat: state => key => {
    for (let item of state.subjects) {
      if (item.id === key) {
        return item.name + " ( " + item.levelName + " )";
      }
    }
    return null;
  }
};

// actions
const actions = {
  initSubject({ commit }, action) {
    subjectApi.list().then(re => {
      commit("setSubjects", re.response);
      if (action !== undefined) {
        action();
      }
    });
  },

  initTopic({ commit }, action) {
    topicApi.list().then(re => {
      commit("setTopics", re.response);
      if (action !== undefined) {
        action();
      }
    });
  }
};

// mutations
const mutations = {
  setSubjects: (state, subjects) => {
    state.subjects = subjects;
  },
  setTopics: (state, topics) => {
    state.topics = topics;
  }
};

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
};
