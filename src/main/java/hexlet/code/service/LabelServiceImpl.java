package hexlet.code.service;

import hexlet.code.dto.LabelDto;
import hexlet.code.exception.InvalidElementException;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class LabelServiceImpl implements LabelService {

    private LabelRepository labelRepository;

    @Override
    public List<Label> getAllLabels() {
        return labelRepository.findAll();
    }

    @Override
    public Label getLabelById(long id) {
        return labelRepository.findById(id)
                .orElseThrow(() -> InvalidElementException.invalidElement("Label not found"));
    }

    @Override
    public Label createLabel(final LabelDto labelDto) {
        Label newLabel = new Label();
        newLabel.setName(labelDto.getName());
        return labelRepository.save(newLabel);
    }

    @Override
    public Label updateLabel(final LabelDto labelDto, final long id) {
        Label label = getLabelById(id);
        label.setName(labelDto.getName());
        return labelRepository.save(label);
    }

    @Override
    public void deleteLabel(long id) {
        Label label = getLabelById(id);
        labelRepository.delete(label);
    }
}
